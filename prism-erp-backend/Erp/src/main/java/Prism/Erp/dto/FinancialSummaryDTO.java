package Prism.Erp.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FinancialSummaryDTO {
    private BigDecimal totalReceivables;
    private BigDecimal totalOverdue;
    private BigDecimal totalPaid;
    private BigDecimal totalPending;
    private Integer overdueCount;
    private Integer pendingCount;
}
