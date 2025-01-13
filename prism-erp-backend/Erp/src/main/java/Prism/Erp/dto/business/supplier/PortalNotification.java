package Prism.Erp.dto.business.supplier;

import Prism.Erp.entity.business.Tenant.TenantAwareEntity;
import Prism.Erp.model.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "portal_notifications")
@Getter
@Setter
public class PortalNotification extends TenantAwareEntity {

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private SupplierPortalAccount account;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(nullable = false)
    private boolean read;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String referenceType;

    private String referenceId;
}

