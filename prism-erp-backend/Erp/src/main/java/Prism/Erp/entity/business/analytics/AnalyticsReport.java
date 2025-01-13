package Prism.Erp.entity.business.analytics;

import Prism.Erp.entity.business.Tenant.TenantAwareEntity;
import Prism.Erp.model.business.ReportType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "analytics_reports")
@Getter
@Setter
public class AnalyticsReport extends TenantAwareEntity {

    @Column(nullable = false)
    private String reportName;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType type;

    @Convert(converter = JsonConverter.class)
    private Map<String, Object> parameters;

    @Column(columnDefinition = "jsonb")
    private String data;

    @Column(nullable = false)
    private LocalDateTime generatedAt;

    private LocalDateTime validUntil;

    @Column(nullable = false)
    private String generatedBy;

    private String notes;
}
