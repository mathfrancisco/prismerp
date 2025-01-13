package Prism.Erp.repository.business.purchase;

import Prism.Erp.entity.business.purchase.PurchaseOrderHistory;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchaseOrderHistoryRepository extends JpaRepository<PurchaseOrderHistory, Long> {
    Optional<PurchaseOrderHistory> findByPurchaseOrderId(Long purchaseOrderId);
    
    @Query("SELECT poh FROM PurchaseOrderHistory poh JOIN FETCH poh.events WHERE poh.purchaseOrder.id = :purchaseOrderId")
    Optional<PurchaseOrderHistory> findByPurchaseOrderIdWithEvents(@Param("purchaseOrderId") Long purchaseOrderId);
}