package Prism.Erp.dto.business.analytics;

import Prism.Erp.dto.business.dashboard.ChartDataDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {
    private Map<String, Object> metrics;
    private List<ChartDataDTO> charts;
    private List<AlertDTO> alerts;
    private Map<String, TrendDTO> trends;
}
