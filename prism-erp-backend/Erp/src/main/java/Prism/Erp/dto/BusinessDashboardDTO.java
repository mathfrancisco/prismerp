@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessDashboardDTO {
    private LocalDateTime lastUpdate;
    private Map<String, BigDecimal> financialSummary;
    private Map<String, Integer> operationalMetrics;
    private List<DeviationDTO> alerts;
    private Map<String, List<ChartDataDTO>> charts;
    private Map<String, KpiDTO> kpis;
}
