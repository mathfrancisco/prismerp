package Prism.Erp.entity.business.inventory;

import Prism.Erp.entity.business.Tenant.TenantAwareEntity;
import Prism.Erp.model.business.MovementType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_movements")
@Getter
@Setter
public class InventoryMovement extends TenantAwareEntity {

    @ManyToOne(optional = false)
    private InventoryItem item;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovementType type;

    @Column(nullable = false)
    private BigDecimal quantity;

    @Column(nullable = false)
    private String referenceType;

    @Column(nullable = false)
    private String referenceId;

    private String notes;

    @Column(nullable = false)
    private LocalDateTime movementDate;

    @Column(nullable = false)
    private String requestedBy;
}
