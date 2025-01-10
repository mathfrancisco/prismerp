package Prism.Erp.service.impl;

import Prism.Erp.dto.SaleDTO;
import Prism.Erp.dto.SaleItemDTO;
import Prism.Erp.dto.InventoryTransactionDTO;
import Prism.Erp.entity.SalesOrderItem;
import Prism.Erp.exception.BusinessException;
import Prism.Erp.exception.InsufficientStockException;
import Prism.Erp.exception.ResourceNotFoundException;
import Prism.Erp.model.TransactionType;
import Prism.Erp.repository.ProductRepository;
import Prism.Erp.repository.InventoryTransactionRepository;
import Prism.Erp.service.InventoryService;
import Prism.Erp.service.SaleService;
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
    private final SaleService saleService;
    private final ProductRepository productRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;
    
    @Autowired
    public InventorySalesIntegrationServiceImpl(
            InventoryService inventoryService,
            SaleService saleService,
            ProductRepository productRepository,
            InventoryTransactionRepository inventoryTransactionRepository) {
        this.inventoryService = inventoryService;
        this.saleService = saleService;
        this.productRepository = productRepository;
        this.inventoryTransactionRepository = inventoryTransactionRepository;
    }

    @Override
    @Transactional
    public void processSaleInventoryTransaction(SaleDTO sale) {
        log.info("Processing inventory transaction for sale: {}", sale.getSaleNumber());
        validateStockAvailability(sale.getItems());
        
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
    @Transactional
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
    public void validateStockAvailability(List<SaleItemDTO> items) {
        log.info("Validating stock availability for {} items", items.size());
        List<String> errors = new ArrayList<>();
        
        items.forEach(item -> {
            ProductStockDTO stock = inventoryService.getProductStock(item.getProductId());
            if (stock == null) {
                errors.add(String.format("Product stock not found for product ID: %d", 
                    item.getProductId()));
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
