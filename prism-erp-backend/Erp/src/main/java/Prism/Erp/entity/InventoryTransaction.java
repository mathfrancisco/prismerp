package Prism.Erp.entity;

import Prism.Erp.model.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventory_transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryTransaction extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private Integer quantity;

    private String reference;

    private String notes;
}