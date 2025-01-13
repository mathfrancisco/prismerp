package Prism.Erp.entity.business.Audit;

import Prism.Erp.model.business.AuditAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogDTO {
    private Long id;
    private String entityType;
    private String entityId;
    private AuditAction action;
    private String username;
    private String oldValue;
    private String newValue;
    private String description;
    private String ipAddress;
    private LocalDateTime timestamp;
}
