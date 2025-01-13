package Prism.Erp.dto.business.supplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
