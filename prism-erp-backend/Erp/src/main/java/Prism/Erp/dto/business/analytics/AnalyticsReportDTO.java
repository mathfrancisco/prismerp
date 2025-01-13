package Prism.Erp.dto.business.analytics;

import Prism.Erp.model.business.ReportType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsReportDTO {
    private Long id;
    private String reportName;
    private ReportType type;
    private Map<String, Object> parameters;
    private Map<String, Object> data;
    private LocalDateTime generatedAt;
    private String generatedBy;
    private List<AnomalyDTO> anomalies;
}
