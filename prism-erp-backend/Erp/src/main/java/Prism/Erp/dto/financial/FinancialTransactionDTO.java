package Prism.Erp.dto.financial;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class FinancialTransactionDTO {
    private Long id;
    private String transactionNumber;
    private String type;
    private Long accountReceivableId;
    private BigDecimal amount;
    private LocalDate transactionDate;
    private String description;
    private String paymentMethod;
    private String bankAccount;
}
