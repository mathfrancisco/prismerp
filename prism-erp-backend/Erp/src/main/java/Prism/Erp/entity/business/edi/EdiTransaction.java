package Prism.Erp.entity.business.edi;

import Prism.Erp.entity.business.Tenant.TenantAwareEntity;
import Prism.Erp.model.business.EdiStatus;
import Prism.Erp.model.business.EdiTransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "edi_transactions")
@Getter
@Setter
public class EdiTransaction extends TenantAwareEntity {

    @ManyToOne
    @JoinColumn(name = "configuration_id", nullable = false)
    private EdiConfiguration configuration;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EdiTransactionType type;

    @Column(nullable = false)
    private String referenceNumber;

    @Column(columnDefinition = "text")
    private String rawMessage;

    @Column(columnDefinition = "jsonb")
    private String processedData;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EdiStatus status;

    private String errorMessage;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime processedAt;
}