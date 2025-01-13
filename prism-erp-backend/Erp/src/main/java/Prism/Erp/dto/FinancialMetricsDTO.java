@Data
@Builder
public class FinancialMetricsDTO {
    private BigDecimal currentRatio;
    private BigDecimal quickRatio;
    private BigDecimal debtToEquityRatio;
    private BigDecimal operatingMargin;
    private BigDecimal netProfitMargin;
    private Integer averageCollectionPeriod;
    private Integer averagePaymentPeriod;
}
