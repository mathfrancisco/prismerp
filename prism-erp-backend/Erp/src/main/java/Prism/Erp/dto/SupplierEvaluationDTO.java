@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierEvaluationDTO {
    private Long supplierId;
    private BigDecimal qualityScore;
    private BigDecimal deliveryScore;
    private BigDecimal priceCompetitiveness;
    private BigDecimal communicationScore;
    private BigDecimal overallScore;
    private List<String> strengths;
    private List<String> improvements;
    private LocalDate evaluationDate;
    private String evaluatedBy;
}
