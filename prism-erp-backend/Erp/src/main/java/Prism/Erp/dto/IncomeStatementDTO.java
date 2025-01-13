@Data
@Builder
public class IncomeStatementDTO {
    private BigDecimal grossRevenue;
    private BigDecimal netRevenue;
    private BigDecimal costOfGoodsSold;
    private BigDecimal grossProfit;
    private BigDecimal operatingExpenses;
    private BigDecimal operatingIncome;
    private BigDecimal netIncome;
    private List<IncomeStatementEntryDTO> entries;
}
