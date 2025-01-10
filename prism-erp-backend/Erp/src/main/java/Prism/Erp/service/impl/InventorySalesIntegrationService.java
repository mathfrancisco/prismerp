package Prism.Erp.service.impl;

import Prism.Erp.dto.SaleItemDTO;
import Prism.Erp.dto.InventoryTransactionDTO;
import Prism.Erp.dto.SaleDTO;
import Prism.Erp.entity.SalesOrderItem;
import Prism.Erp.exception.InsufficientStockException;
import Prism.Erp.service.InventoryService;
import Prism.Erp.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventorySalesIntegrationService {

    private final InventoryService inventoryService;
    private final SaleService saleService;

    @Transactional
    public void processSaleInventoryTransaction(SaleDTO sale) {
        // Verificar disponibilidade de estoque para todos os itens
        validateStockAvailability(sale.getItems());

        // Processar cada item da venda
        for (SaleItemDTO item : sale.getItems()) {
            createInventoryTransaction(item);
        }
    }

    private void validateStockAvailability(List<SaleItemDTO> items) {
        for (SaleItemDTO item : items) {
            var stockLevel = inventoryService.getProductStock(item.getProductId());
            if (stockLevel.getCurrentStock() < item.getQuantity()) {
                throw new InsufficientStockException(
                        String.format("Insufficient stock for product %s. Available: %d, Requested: %d",
                                stockLevel.getProductName(),
                                stockLevel.getCurrentStock(),
                                item.getQuantity())
                );
            }
        }
    }

    private void createInventoryTransaction(SaleItemDTO item) {
        var transaction = InventoryTransactionDTO.builder()
                .productId(item.getProductId())
                .type("OUTBOUND")
                .quantity(item.getQuantity())
                .reference("SALE-" + item.getSaleId())
                .notes("Automatic inventory update from sale")
                .build();

        inventoryService.createTransaction(transaction);
    }

    @Transactional
    public void cancelSaleInventoryTransaction(Long saleId, List<SaleItemDTO> items) {
        for (SaleItemDTO item : items) {
            var transaction = InventoryTransactionDTO.builder()
                    .productId(item.getProductId())
                    .type("INBOUND")
                    .quantity(item.getQuantity())
                    .reference("SALE-CANCEL-" + saleId)
                    .notes("Sale cancellation inventory return")
                    .build();

            inventoryService.createTransaction(transaction);
        }
    }

    public void reserveInventory(Long id, List<SalesOrderItem> items) {
    }
}