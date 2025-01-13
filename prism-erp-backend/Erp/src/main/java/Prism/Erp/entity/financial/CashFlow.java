package Prism.Erp.entity.financial;

import Prism.Erp.entity.BaseEntity;
import Prism.Erp.model.TransactionType;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class CashFlow extends BaseEntity {
    private LocalDate date;
    private BigDecimal amount;
    private TransactionType type;
    private String description;
    private CostCenter costCenter;
    private String category;
    private BankAccount bankAccount;
}
