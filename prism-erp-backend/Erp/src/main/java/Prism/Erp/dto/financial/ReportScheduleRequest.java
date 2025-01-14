package Prism.Erp.dto.financial;

import Prism.Erp.model.financial.ExportFormat;
import Prism.Erp.model.financial.ReportType;
import Prism.Erp.model.financial.ScheduleFrequency;
import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class ReportScheduleRequest {
    private ReportType reportType;
    private ScheduleFrequency frequency;
    private List<ExportFormat> formats;
    private List<String> recipients;
    private LocalTime executionTime;
    private Set<DayOfWeek> executionDays;
}
