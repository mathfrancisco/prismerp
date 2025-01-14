package Prism.Erp.entity.financial;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "approval_workflow_config")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalWorkflowConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String workflowType; // PAYMENT, PURCHASE, etc

    private BigDecimal minimumAmount;

    private BigDecimal maximumAmount;

    private Integer requiredApprovals;

    @ElementCollection
    private List<String> approverRoles;

    private Boolean sequentialApproval;
}

