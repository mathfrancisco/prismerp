package Prism.Erp.repository.business.purchase;

import Prism.Erp.entity.business.purchase.PurchaseOrderItem;
import Prism.Erp.model.business.PurchaseOrderStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItem, Long> {
    List<PurchaseOrderItem> findByPurchaseOrderId(Long purchaseOrderId);
    
    @Query("SELECT poi FROM PurchaseOrderItem poi WHERE poi.productId = :productId AND poi.purchaseOrder.status = :status")
    List<PurchaseOrderItem> findByProductIdAndStatus(
        @Param("productId") Long productId,
        @Param("status") PurchaseOrderStatus status
    );
    
    @Query("SELECT SUM(poi.quantity) FROM PurchaseOrderItem poi WHERE poi.productId = :productId AND poi.purchaseOrder.status != 'CANCELLED'")
    BigDecimal getTotalOrderedQuantityForProduct(@Param("productId") Long productId);
}