package Prism.Erp.repository.business.purchase;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderApprovalRepository extends JpaRepository<PurchaseOrderApproval, Long> {
    List<PurchaseOrderApproval> findByPurchaseOrderId(Long purchaseOrderId);
    
    @Query("SELECT poa FROM PurchaseOrderApproval poa WHERE poa.purchaseOrder.id = :purchaseOrderId AND poa.status = :status")
    List<PurchaseOrderApproval> findByPurchaseOrderIdAndStatus(
        @Param("purchaseOrderId") Long purchaseOrderId,
        @Param("status") ApprovalStatus status
    );
    
    @Query("SELECT CASE WHEN COUNT(poa) > 0 THEN true ELSE false END FROM PurchaseOrderApproval poa " +
           "WHERE poa.purchaseOrder.id = :purchaseOrderId AND poa.status = 'APPROVED'")
    boolean hasApprovedStatus(@Param("purchaseOrderId") Long purchaseOrderId);
}