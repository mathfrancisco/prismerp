package Prism.Erp.entity.business.purchase;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchase_order_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id", nullable = false)
    private PurchaseOrder purchaseOrder;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String productCode;

    @Column(nullable = false)
    private String productName;

    @Column(precision = 19, scale = 4, nullable = false)
    private BigDecimal quantity;

    @Column(nullable = false)
    private String unit;

    @Column(precision = 19, scale = 4, nullable = false)
    private BigDecimal unitPrice;

    @Column(precision = 19, scale = 4)
    private BigDecimal discount;

    @Column(precision = 5, scale = 2)
    private BigDecimal taxRate;

    @Column(precision = 19, scale = 4)
    private BigDecimal totalPrice;

    private LocalDateTime expectedDeliveryDate;

    private String notes;
}
