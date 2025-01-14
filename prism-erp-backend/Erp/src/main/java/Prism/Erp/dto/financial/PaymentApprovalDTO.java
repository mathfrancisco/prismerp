package Prism.Erp.dto.financial;

import Prism.Erp.model.financial.ApprovalStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PaymentApprovalDTO {
    private Long id;
    private Long accountPayableId;
    private Long approverId;
    private LocalDateTime approvalDate;
    private ApprovalStatus status;
    private String comments;
    private Integer approvalLevel;
}
