package Prism.Erp.entity;

import Prism.Erp.model.TransactionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "financial_transactions")
@Data
@EqualsAndHashCode(callSuper = true)
public class FinancialTransaction extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String transactionNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @ManyToOne
    @JoinColumn(name = "account_receivable_id")
    private AccountReceivable accountReceivable;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate transactionDate;

    private String description;

    private String paymentMethod;

    private String bankAccount;
}