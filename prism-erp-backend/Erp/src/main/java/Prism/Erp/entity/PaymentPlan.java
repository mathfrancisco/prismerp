package Prism.Erp.entity;

import Prism.Erp.model.PaymentFrequency;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payment_plans")
@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentPlan extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @Column(nullable = false)
    private Integer numberOfInstallments;

    @Column(nullable = false)
    private BigDecimal installmentAmount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentFrequency frequency;

    @Column(nullable = false)
    private LocalDate firstDueDate;

    private BigDecimal interestRate;

    private String paymentMethod;
}
