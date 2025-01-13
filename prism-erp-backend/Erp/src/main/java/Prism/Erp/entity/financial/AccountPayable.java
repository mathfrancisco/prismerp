package Prism.Erp.entity.financial;

import Prism.Erp.entity.BaseEntity;
import Prism.Erp.entity.business.supplier.Supplier;
import Prism.Erp.model.financial.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "account_payable")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountPayable extends BaseEntity {
    @Column(nullable = false)
    private String documentNumber;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate dueDate;

    private LocalDate issueDate;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String description;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "cost_center_id")
    private CostCenter costCenter;

    @OneToMany(mappedBy = "accountPayable", cascade = CascadeType.ALL)
    private List<PaymentSchedule> schedules;

    @OneToMany(mappedBy = "accountPayable", cascade = CascadeType.ALL)
    private List<PaymentApproval> approvals;
}