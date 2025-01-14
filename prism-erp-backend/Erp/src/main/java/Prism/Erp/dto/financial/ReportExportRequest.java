package Prism.Erp.dto.financial;

import Prism.Erp.model.financial.ExportFormat;
import Prism.Erp.model.financial.ReportType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ReportExportRequest {
    private ReportType reportType;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate referenceDate;
    private Long costCenterId;
    private List<ExportFormat> formats;
    private String requestedBy;
}
