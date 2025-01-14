package Prism.Erp.dto.financial;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ReconciliationItemDTO {
    private Long id;
    private LocalDate transactionDate;
    private String description;
    private BigDecimal amount;
    private Boolean reconciled;
    private String reference;
}