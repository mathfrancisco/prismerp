package Prism.Erp.repository.business.purchase;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    Optional<PurchaseOrder> findByOrderNumber(String orderNumber);
    
    List<PurchaseOrder> findBySupplierIdAndStatus(Long supplierId, PurchaseOrderStatus status);
    
    @Query("SELECT po FROM PurchaseOrder po WHERE po.status = :status AND po.expectedDeliveryDate < :date")
    List<PurchaseOrder> findOverduePurchaseOrders(@Param("status") PurchaseOrderStatus status, @Param("date") LocalDateTime date);
    
    @Query("SELECT NEW com.prism.erp.dto.business.purchase.PurchaseAnalyticsDTO(COUNT(po), SUM(po.totalAmount)) " +
           "FROM PurchaseOrder po WHERE po.orderDate BETWEEN :startDate AND :endDate")
    PurchaseAnalyticsDTO getPurchaseAnalytics(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
