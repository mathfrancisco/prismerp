package Prism.Erp.dto.financial;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class BankReconciliationDTO {
    private Long bankAccountId;
    private LocalDate reconciliationDate;
    private BigDecimal bankBalance;
    private BigDecimal systemBalance;
    private BigDecimal difference;
    private List<ReconciliationItemDTO> pendingItems;
    private List<ReconciliationItemDTO> matchedItems;
}
