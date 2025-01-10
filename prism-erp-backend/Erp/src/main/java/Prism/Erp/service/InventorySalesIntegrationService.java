package Prism.Erp.service;

import Prism.Erp.dto.SaleDTO;
import Prism.Erp.dto.SaleItemDTO;
import Prism.Erp.entity.SalesOrderItem;
import java.util.List;

public interface InventorySalesIntegrationService {
    void processSaleInventoryTransaction(SaleDTO sale);
    void reserveInventory(Long orderId, List<SalesOrderItem> items);
    void confirmInventoryReduction(Long orderId, List<SalesOrderItem> items);
    void releaseInventory(Long orderId, List<SalesOrderItem> items);
    void cancelSaleInventoryTransaction(Long saleId, List<SaleItemDTO> items);
    void processReturnInventoryTransaction(Long returnId, List<SaleItemDTO> items);
    boolean hasAvailableStock(Long productId, int quantity);
    void validateStockAvailability(List<SalesOrderItem> items);
}