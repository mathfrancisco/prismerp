package Prism.Erp.dto.business.purchase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderApprovalDTO {
    private Long id;
    private Long purchaseOrderId;
    private String approverName;
    private String approverRole;
    private ApprovalStatus status;
    private LocalDateTime approvalDate;
    private String comments;
}