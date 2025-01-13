package Prism.Erp.dto.business.dashboard;

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
public class ChartDataDTO {
    private String label;
    private Object value;
    private String category;
    private LocalDateTime timestamp;
    private Map<String, Object> metadata;
}
