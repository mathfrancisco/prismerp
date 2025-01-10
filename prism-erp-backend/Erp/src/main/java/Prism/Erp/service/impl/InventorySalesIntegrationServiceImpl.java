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
@Transactional
@Slf4j
public class InventorySalesIntegrationServiceImpl implements InventorySalesIntegrationService {
    
    private final InventoryService inventoryService;
    private final ProductRepository productRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;
    
    @Autowired
    public InventorySalesIntegrationServiceImpl(
            InventoryService inventoryService,
            ProductRepository productRepository,
            InventoryTransactionRepository inventoryTransactionRepository) {
        this.inventoryService = inventoryService;
        this.productRepository = productRepository;
        this.inventoryTransactionRepository = inventoryTransactionRepository;
    }

    @Override
    public void processSaleInventoryTransaction(SaleDTO sale) {
        log.info("Processing inventory transaction for sale: {}", sale.getSaleNumber());
        sale.getItems().forEach(item -> {
            InventoryTransactionDTO transactionDTO = InventoryTransactionDTO.builder()
                .productId(item.getProductId())
                .quantity(item.getQuantity())
                .transactionType(TransactionType.SALE)
                .referenceId(sale.getId())
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
                .transactionType(TransactionType.RESERVE)
                .referenceId(orderId)
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
                .transactionType(TransactionType.OUTBOUND)
                .referenceId(orderId)
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
                .transactionType(TransactionType.RELEASE)
                .referenceId(orderId)
                .notes("Order reservation release: " + orderId)
                .build();
            
            inventoryService.createTransaction(transactionDTO);
        });
        log.info("Inventory released successfully for order: {}", orderId);
    }

    @Override
    public void cancelSaleInventoryTransaction(Long saleId, List<SaleItemDTO> items) {
        log.info("Canceling inventory transaction for sale: {}", saleId);
        items.forEach(item -> {
            InventoryTransactionDTO transactionDTO = InventoryTransactionDTO.builder()
                .productId(item.getProductId())
                .quantity(item.getQuantity())
                .transactionType(TransactionType.REVERSAL)
                .referenceId(saleId)
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
                .transactionType(TransactionType.RETURN)
                .referenceId(returnId)
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
                errors.add(String.format("Product stock not found for product: %s", 
                    item.getProduct().getCode()));
            } else if (stock.getCurrentStock() < item.getQuantity()) {
                errors.add(String.format("Insufficient stock for product %s. Available: %d, Requested: %d",
                    item.getProduct().getCode(), stock.getCurrentStock(), item.getQuantity()));
            }
        });
        
        if (!errors.isEmpty()) {
            log.error("Stock validation failed: {}", errors);
            throw new InsufficientStockException(String.join("; ", errors));
        }
        log.info("Stock validation successful for all items");
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
