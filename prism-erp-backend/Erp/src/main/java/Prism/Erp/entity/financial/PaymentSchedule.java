package Prism.Erp.entity.financial;

import Prism.Erp.entity.BaseEntity;
import Prism.Erp.model.financial.PaymentStatus;
import Prism.Erp.model.financial.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payment_schedule")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSchedule extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "account_payable_id", nullable = false)
    private AccountPayable accountPayable;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate scheduledDate;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    private String bankAccount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}
