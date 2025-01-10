package Prism.Erp.service.impl;

import Prism.Erp.dto.SaleDTO;
import Prism.Erp.dto.SaleItemDTO;
import Prism.Erp.entity.InventoryTransaction;
import Prism.Erp.entity.Product;
import Prism.Erp.entity.ProductStock;
import Prism.Erp.entity.SalesOrderItem;
import Prism.Erp.exception.BusinessException;
import Prism.Erp.exception.InsufficientStockException;
import Prism.Erp.model.TransactionType;
import Prism.Erp.repository.ProductRepository;
import Prism.Erp.repository.ProductStockRepository;
import Prism.Erp.repository.InventoryTransactionRepository;

import Prism.Erp.service.InventoryService;
import Prism.Erp.service.SaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventorySalesIntegrationServiceImpl extends InventorySalesIntegrationService {

    private final ProductStockRepository productStockRepository;
    private final ProductRepository productRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;

    // Cache de reservas para evitar condições de corrida
    private final Map<Long, Integer> reservationCache = new ConcurrentHashMap<>();

    public InventorySalesIntegrationServiceImpl(InventoryService inventoryService, SaleService saleService, ProductStockRepository productStockRepository, ProductRepository productRepository, InventoryTransactionRepository inventoryTransactionRepository) {
        super(inventoryService, saleService);
        this.productStockRepository = productStockRepository;
        this.productRepository = productRepository;
        this.inventoryTransactionRepository = inventoryTransactionRepository;
    }

    @Override
    @Transactional
    public void processSaleInventoryTransaction(SaleDTO sale) {
        log.info("Processing inventory transaction for sale {}", sale.getId());

        List<SalesOrderItem> items = sale.getItems().stream()
                .map(item -> SalesOrderItem.builder()
                        .product(productRepository.findById(item.getProductId())
                                .orElseThrow(() -> new BusinessException("Product not found: " + item.getProductId())))
                        .quantity(item.getQuantity())
                        .build())
                .collect(Collectors.toList());

        try {
            // Primeiro valida disponibilidade
            validateStockAvailability(items);

            // Reserva o estoque
            reserveInventory(sale.getId(), items);

            // Confirma a redução do estoque
            confirmInventoryReduction(sale.getId(), items);

            log.info("Successfully processed inventory transaction for sale {}", sale.getId());
        } catch (Exception e) {
            log.error("Error processing inventory transaction for sale {}: {}", sale.getId(), e.getMessage());
            throw new BusinessException("Failed to process inventory transaction: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void reserveInventory(Long orderId, List<SalesOrderItem> items) {
        log.info("Reserving inventory for order {}", orderId);

        Map<Long, Integer> quantityByProduct = items.stream()
                .collect(Collectors.groupingBy(
                        item -> item.getProduct().getId(),
                        Collectors.summingInt(SalesOrderItem::getQuantity)
                ));

        synchronized (reservationCache) {
            for (Map.Entry<Long, Integer> entry : quantityByProduct.entrySet()) {
                Long productId = entry.getKey();
                Integer quantity = entry.getValue();

                ProductStock stock = productStockRepository.findByProductId(productId)
                        .orElseThrow(() -> new BusinessException("Stock not found for product " + productId));

                // Verificar disponibilidade considerando reservas
                int reserved = reservationCache.getOrDefault(productId, 0);
                int available = stock.getCurrentQuantity() - reserved;

                if (available < quantity) {
                    throw new InsufficientStockException("Insufficient stock for product " + productId +
                            " (Available: " + available + ", Requested: " + quantity + ")");
                }

                // Atualizar cache de reservas
                reservationCache.merge(productId, quantity, Integer::sum);

                // Registrar movimento de reserva
                registerStockMovement(stock, quantity, TransactionType.RESERVE, orderId);

                log.info("Reserved {} units of product {} for order {}", quantity, productId, orderId);
            }
        }
    }

    @Override
    @Transactional
    public void confirmInventoryReduction(Long orderId, List<SalesOrderItem> items) {
        log.info("Confirming inventory reduction for order {}", orderId);

        synchronized (reservationCache) {
            for (SalesOrderItem item : items) {
                Long productId = item.getProduct().getId();
                int quantity = item.getQuantity();

                ProductStock stock = productStockRepository.findByProductId(productId)
                        .orElseThrow(() -> new BusinessException("Stock not found for product " + productId));

                // Remover do cache de reservas
                reservationCache.computeIfPresent(productId, (k, v) -> v - quantity);

                // Atualizar estoque físico
                stock.setCurrentQuantity(stock.getCurrentQuantity() - quantity);
                if (stock.getCurrentQuantity() < stock.getMinimumQuantity()) {
                    log.warn("Stock below minimum for product {} (Current: {}, Minimum: {})",
                            productId, stock.getCurrentQuantity(), stock.getMinimumQuantity());
                }

                // Registrar movimento de saída
                registerStockMovement(stock, quantity, TransactionType.SALE, orderId);

                productStockRepository.save(stock);

                log.info("Confirmed reduction of {} units for product {} in order {}",
                        quantity, productId, orderId);
            }
        }
    }

    @Override
    @Transactional
    public void releaseInventory(Long orderId, List<SalesOrderItem> items) {
        log.info("Releasing inventory for order {}", orderId);

        synchronized (reservationCache) {
            for (SalesOrderItem item : items) {
                Long productId = item.getProduct().getId();
                int quantity = item.getQuantity();

                // Remover do cache de reservas
                reservationCache.computeIfPresent(productId, (k, v) -> v - quantity);

                ProductStock stock = productStockRepository.findByProductId(productId)
                        .orElseThrow(() -> new BusinessException("Stock not found for product " + productId));

                // Registrar movimento de liberação
                registerStockMovement(stock, quantity, TransactionType.RELEASE, orderId);

                log.info("Released {} units of product {} for order {}", quantity, productId, orderId);
            }
        }
    }

    @Override
    @Transactional
    public void cancelSaleInventoryTransaction(Long saleId, List<SaleItemDTO> items) {
        log.info("Canceling inventory transaction for sale {}", saleId);

        List<SalesOrderItem> orderItems = items.stream()
                .map(item -> SalesOrderItem.builder()
                        .product(productRepository.findById(item.getProductId())
                                .orElseThrow(() -> new BusinessException("Product not found: " + item.getProductId())))
                        .quantity(item.getQuantity())
                        .build())
                .collect(Collectors.toList());

        try {
            for (SalesOrderItem item : orderItems) {
                ProductStock stock = productStockRepository.findByProductId(item.getProduct().getId())
                        .orElseThrow(() -> new BusinessException("Stock not found for product"));

                // Restaura o estoque
                stock.setCurrentQuantity(stock.getCurrentQuantity() + item.getQuantity());
                productStockRepository.save(stock);

                // Registra movimento de estorno
                registerStockMovement(stock, item.getQuantity(), TransactionType.REVERSAL, saleId);

                log.info("Reversed {} units for product {} in sale {}",
                        item.getQuantity(), item.getProduct().getId(), saleId);
            }
        } catch (Exception e) {
            log.error("Error canceling inventory transaction for sale {}: {}", saleId, e.getMessage());
            throw new BusinessException("Failed to cancel inventory transaction: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void processReturnInventoryTransaction(Long returnId, List<SaleItemDTO> items) {
        log.info("Processing return inventory transaction {}", returnId);

        List<SalesOrderItem> orderItems = items.stream()
                .map(item -> SalesOrderItem.builder()
                        .product(productRepository.findById(item.getProductId())
                                .orElseThrow(() -> new BusinessException("Product not found: " + item.getProductId())))
                        .quantity(item.getQuantity())
                        .build())
                .collect(Collectors.toList());

        try {
            for (SalesOrderItem item : orderItems) {
                ProductStock stock = productStockRepository.findByProductId(item.getProduct().getId())
                        .orElseThrow(() -> new BusinessException("Stock not found for product"));

                // Incrementa o estoque
                stock.setCurrentQuantity(stock.getCurrentQuantity() + item.getQuantity());
                productStockRepository.save(stock);

                // Registra movimento de devolução
                registerStockMovement(stock, item.getQuantity(), TransactionType.RETURN, returnId);

                log.info("Processed return of {} units for product {} in return {}",
                        item.getQuantity(), item.getProduct().getId(), returnId);
            }
        } catch (Exception e) {
            log.error("Error processing return inventory transaction {}: {}", returnId, e.getMessage());
            throw new BusinessException("Failed to process return inventory transaction: " + e.getMessage());
        }
    }

    @Override
    public boolean hasAvailableStock(Long productId, int quantity) {
        ProductStock stock = productStockRepository.findByProductId(productId)
                .orElseThrow(() -> new BusinessException("Stock not found for product " + productId));

        synchronized (reservationCache) {
            int reserved = reservationCache.getOrDefault(productId, 0);
            int available = stock.getCurrentQuantity() - reserved;

            log.debug("Stock check for product {}: Current={}, Reserved={}, Available={}, Requested={}",
                    productId, stock.getCurrentQuantity(), reserved, available, quantity);

            return available >= quantity;
        }
    }

    @Override
    public void validateStockAvailability(List<SalesOrderItem> items) {
        log.info("Validating stock availability for {} items", items.size());

        Map<Long, Integer> quantityByProduct = items.stream()
                .collect(Collectors.groupingBy(
                        item -> item.getProduct().getId(),
                        Collectors.summingInt(SalesOrderItem::getQuantity)
                ));

        for (Map.Entry<Long, Integer> entry : quantityByProduct.entrySet()) {
            if (!hasAvailableStock(entry.getKey(), entry.getValue())) {
                Product product = productRepository.findById(entry.getKey())
                        .orElseThrow(() -> new BusinessException("Product not found"));

                log.warn("Insufficient stock for product {}: {}", product.getId(), product.getName());

                throw new InsufficientStockException(
                        "Insufficient stock for product: " + product.getName());
            }
        }

        log.info("Stock availability validation successful");
    }

    private void registerStockMovement(ProductStock stock, int quantity, TransactionType type, Long referenceId) {
        InventoryTransaction transaction = InventoryTransaction.builder()
                .product(stock.getProduct())
                .quantity(quantity)
                .transactionType(type)
                .transactionDate(LocalDateTime.now())
                .referenceId(referenceId)
                .previousQuantity(stock.getCurrentQuantity())
                .newQuantity(type.isDecrease() ?
                        stock.getCurrentQuantity() - quantity :
                        stock.getCurrentQuantity() + quantity)
                .build();

        inventoryTransactionRepository.save(transaction);

        log.debug("Registered stock movement: {} units of product {} for transaction type {} (Reference: {})",
                quantity, stock.getProduct().getId(), type, referenceId);
    }
}