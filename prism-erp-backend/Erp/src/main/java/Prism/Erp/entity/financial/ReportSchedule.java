package Prism.Erp.entity.financial;

import Prism.Erp.model.financial.ExportFormat;
import Prism.Erp.model.financial.ReportType;
import Prism.Erp.model.financial.ScheduleFrequency;
import Prism.Erp.model.financial.ScheduleStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "report_schedule")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Enumerated(EnumType.STRING)
    private ScheduleFrequency frequency;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<ExportFormat> formats;

    @ElementCollection
    private List<String> recipients;

    private LocalDateTime nextExecutionDate;

    private LocalDateTime lastExecutionDate;

    @Enumerated(EnumType.STRING)
    private ScheduleStatus status;
}
