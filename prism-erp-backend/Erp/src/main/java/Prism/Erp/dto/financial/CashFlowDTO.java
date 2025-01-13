package Prism.Erp.dto.financial;

@Data
@Builder
public class CashFlowDTO {
    private BigDecimal openingBalance;
    private List<CashFlowEntryDTO> entries;
    private BigDecimal closingBalance;
    private BigDecimal projectedBalance;
}
