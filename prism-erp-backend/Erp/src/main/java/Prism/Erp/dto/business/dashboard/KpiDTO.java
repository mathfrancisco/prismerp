package Prism.Erp.dto.business.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KpiDTO {
    private String name;
    private String description;
    private Object value;
    private Object target;
    private String unit;
    private String status;
    private BigDecimal trend;
    private LocalDateTime lastUpdate;
}