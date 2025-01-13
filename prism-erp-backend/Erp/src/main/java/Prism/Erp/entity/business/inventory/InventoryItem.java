package Prism.Erp.entity.business.inventory;

import Prism.Erp.entity.business.Tenant.TenantAwareEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Entity
@Table(name = "inventory_items")
@Getter
@Setter
public class InventoryItem extends TenantAwareEntity {

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal quantity;

    @Column(nullable = false)
    private String unit;

    @Column(nullable = false)
    private BigDecimal minimumStock;

    @Column(nullable = false)
    private BigDecimal reorderPoint;

    @Column(nullable = false)
    private BigDecimal maximumStock;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String category;

    @ElementCollection
    private Map<String, String> attributes;

    @Version
    private Long version;
}
