package Prism.Erp.service.impl;

import Prism.Erp.dto.SaleDTO;
import Prism.Erp.dto.SaleItemDTO;
import Prism.Erp.dto.InventoryTransactionDTO;
import Prism.Erp.dto.ProductStockDTO;
import Prism.Erp.entity.SalesOrderItem;
import Prism.Erp.exception.BusinessException;
import Prism.Erp.exception.InsufficientStockException;
import Prism.Erp.exception.ResourceNotFoundException;
import Prism.Erp.model.TransactionType;
import Prism.Erp.repository.ProductRepository;
import Prism.Erp.service.InventorySalesIntegrationService;
import Prism.Erp.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class InventorySalesIntegrationServiceImpl implements InventorySalesIntegrationService {

    private final InventoryService inventoryService;
    private final ProductRepository productRepository;

    @Autowired
    public InventorySalesIntegrationServiceImpl(
            InventoryService inventoryService,
            ProductRepository productRepository) {
        this.inventoryService = inventoryService;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public void processSaleInventoryTransaction(SaleDTO sale) {
        log.info("Processing inventory transaction for sale: {}", sale.getSaleNumber());
        validateStockAvailability(sale.getItems().stream()
                .map(this::convertToSalesOrderItem)
                .collect(Collectors.toList()));

        sale.getItems().forEach(item -> {
            InventoryTransactionDTO transactionDTO = InventoryTransactionDTO.builder()
                    .productId(item.getProductId())
                    .quantity(item.getQuantity())
                    .type(TransactionType.SALE.name())
                    .reference(sale.getId().toString())
                    .notes("Sale transaction: " + sale.getSaleNumber())
                    .build();

            inventoryService.createTransaction(transactionDTO);
        });
        log.info("Inventory transaction processed successfully for sale: {}", sale.getSaleNumber());
    }

    @Override
    public void reserveInventory(Long orderId, List<SalesOrderItem> items) {
        log.info("Reserving inventory for order: {}", orderId);
        validateStockAvailability(items);

        items.forEach(item -> {
            InventoryTransactionDTO transactionDTO = InventoryTransactionDTO.builder()
                    .productId(item.getProduct().getId())
                    .quantity(item.getQuantity())
                    .type(TransactionType.RESERVE.name())
                    .reference(orderId.toString())
                    .notes("Order reservation: " + orderId)
                    .build();

            inventoryService.createTransaction(transactionDTO);
        });
        log.info("Inventory reserved successfully for order: {}", orderId);
    }

    @Override
    public void confirmInventoryReduction(Long orderId, List<SalesOrderItem> items) {
        log.info("Confirming inventory reduction for order: {}", orderId);
        items.forEach(item -> {
            InventoryTransactionDTO transactionDTO = InventoryTransactionDTO.builder()
                    .productId(item.getProduct().getId())
                    .quantity(item.getQuantity())
                    .type(TransactionType.OUTBOUND.name())
                    .reference(orderId.toString())
                    .notes("Order confirmation: " + orderId)
                    .build();

            inventoryService.createTransaction(transactionDTO);
        });
        log.info("Inventory reduction confirmed for order: {}", orderId);
    }

    @Override
    public void releaseInventory(Long orderId, List<SalesOrderItem> items) {
        log.info("Releasing reserved inventory for order: {}", orderId);
        items.forEach(item -> {
            InventoryTransactionDTO transactionDTO = InventoryTransactionDTO.builder()
                    .productId(item.getProduct().getId())
                    .quantity(item.getQuantity())
                    .type(TransactionType.RELEASE.name())
                    .reference(orderId.toString())
                    .notes("Order reservation release: " + orderId)
                    .build();

            inventoryService.createTransaction(transactionDTO);
        });
        log.info("Inventory released successfully for order: {}", orderId);
    }

    @Override
    @Transactional
    public void cancelSaleInventoryTransaction(Long saleId, List<SaleItemDTO> items) {
        log.info("Canceling inventory transaction for sale: {}", saleId);
        items.forEach(item -> {
            InventoryTransactionDTO transactionDTO = InventoryTransactionDTO.builder()
                    .productId(item.getProductId())
                    .quantity(item.getQuantity())
                    .type(TransactionType.REVERSAL.name())
                    .reference(saleId.toString())
                    .notes("Sale cancellation: " + saleId)
                    .build();

            inventoryService.createTransaction(transactionDTO);
        });
        log.info("Inventory transaction canceled for sale: {}", saleId);
    }

    @Override
    public void processReturnInventoryTransaction(Long returnId, List<SaleItemDTO> items) {
        log.info("Processing return inventory transaction: {}", returnId);
        items.forEach(item -> {
            InventoryTransactionDTO transactionDTO = InventoryTransactionDTO.builder()
                    .productId(item.getProductId())
                    .quantity(item.getQuantity())
                    .type(TransactionType.RETURN.name())
                    .reference(returnId.toString())
                    .notes("Product return: " + returnId)
                    .build();

            inventoryService.createTransaction(transactionDTO);
        });
        log.info("Return inventory transaction processed successfully: {}", returnId);
    }

    @Override
    public boolean hasAvailableStock(Long productId, int quantity) {
        ProductStockDTO stock = inventoryService.getProductStock(productId);
        return stock != null && stock.getCurrentStock() >= quantity;
    }

    @Override
    public void validateStockAvailability(List<SalesOrderItem> items) {
        log.info("Validating stock availability for {} items", items.size());
        List<String> errors = new ArrayList<>();

        items.forEach(item -> {
            ProductStockDTO stock = inventoryService.getProductStock(item.getProduct().getId());
            if (stock == null) {
                errors.add(String.format("Product stock not found for product ID: %d",
                        item.getProduct().getId()));
            } else if (stock.getCurrentStock() < item.getQuantity()) {
                errors.add(String.format("Insufficient stock for product %s. Available: %d, Requested: %d",
                        stock.getProductName(), stock.getCurrentStock(), item.getQuantity()));
            }
        });

        if (!errors.isEmpty()) {
            log.error("Stock validation failed: {}", errors);
            throw new InsufficientStockException(String.join("; ", errors));
        }
        log.info("Stock validation successful for all items");
    }

    private SalesOrderItem convertToSalesOrderItem(SaleItemDTO saleItem) {
        SalesOrderItem orderItem = new SalesOrderItem();
        orderItem.setQuantity(saleItem.getQuantity());
        orderItem.getProduct().setId(saleItem.getProductId());
        return orderItem;
    }

    @Transactional(readOnly = true)
    public Map<Long, Integer> getCurrentStockLevels(List<Long> productIds) {
        return productIds.stream()
                .collect(Collectors.toMap(
                        productId -> productId,
                        productId -> {
                            ProductStockDTO stock = inventoryService.getProductStock(productId);
                            return stock != null ? stock.getCurrentStock() : 0;
                        }
                ));
    }

    private void validateProductExists(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
    }
}