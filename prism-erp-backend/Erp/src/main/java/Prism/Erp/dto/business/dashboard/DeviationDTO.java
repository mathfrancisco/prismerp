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
public class DeviationDTO {
    private String type;
    private String description;
    private BigDecimal expectedValue;
    private BigDecimal actualValue;
    private BigDecimal deviation;
    private String impact;
    private LocalDateTime detectedAt;
    private String status;
}