package Prism.Erp.dto.business.analytics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrendDTO {
    private String metric;
    private List<DataPoint> points;
    private Double changePercentage;
    private String trend; // UP, DOWN, STABLE
    private Map<String, Object> analysis;
}
