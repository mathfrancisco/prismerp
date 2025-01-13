package Prism.Erp.entity.financial;

import Prism.Erp.entity.BaseEntity;
import Prism.Erp.entity.User;
import Prism.Erp.model.financial.ApprovalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_approval")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentApproval extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "account_payable_id", nullable = false)
    private AccountPayable accountPayable;

    @ManyToOne
    @JoinColumn(name = "approver_id", nullable = false)
    private User approver;

    @Column(nullable = false)
    private LocalDateTime approvalDate;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus status;

    private String comments;

    private Integer approvalLevel;
}

