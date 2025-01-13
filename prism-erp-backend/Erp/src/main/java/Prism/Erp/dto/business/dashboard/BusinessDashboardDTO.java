package Prism.Erp.dto.business.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
