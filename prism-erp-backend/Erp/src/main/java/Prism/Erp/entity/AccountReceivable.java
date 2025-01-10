package Prism.Erp.entity;

import Prism.Erp.model.ReceivableStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "account_receivables")
@Data
@EqualsAndHashCode(callSuper = true)
public class AccountReceivable extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String documentNumber;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReceivableStatus status;

    private LocalDate paymentDate;

    @Column(nullable = false)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal remainingAmount;

    private String paymentMethod;
}