package Prism.Erp.dto.business.supplier;

import Prism.Erp.entity.business.Tenant.TenantAwareEntity;
import Prism.Erp.entity.business.supplier.Supplier;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "supplier_portal_accounts")
@Getter
@Setter
public class SupplierPortalAccount extends TenantAwareEntity {

    @OneToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private boolean emailVerified;

    @Column(nullable = false)
    private LocalDateTime lastLogin;

    @ElementCollection
    private Set<String> roles;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<PortalNotification> notifications;
}
