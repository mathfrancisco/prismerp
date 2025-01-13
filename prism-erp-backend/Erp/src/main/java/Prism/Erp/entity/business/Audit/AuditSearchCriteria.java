package Prism.Erp.entity.business.Audit;

import Prism.Erp.model.business.AuditAction;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AuditSearchCriteria {
    private String entityType;
    private String entityId;
    private AuditAction action;
    private String username;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String ipAddress;
}
