package Prism.Erp.service.financial;

import Prism.Erp.dto.financial.ReportExportRequest;
import Prism.Erp.dto.financial.ReportScheduleRequest;
import Prism.Erp.entity.financial.ReportSchedule;
import Prism.Erp.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ReportSchedulerService {

    @Autowired
    private ReportExportService exportService;

    @Autowired
    private ReportScheduleRepository scheduleRepository;

    @Autowired
    private NotificationService notificationService;

    /**
     * Agenda geração automática de relatório
     */
    public ReportSchedule scheduleReport(ReportScheduleRequest request) {
        log.info("Agendando relatório: {}", request.getReportType());

        validateScheduleRequest(request);

        ReportSchedule schedule = ReportSchedule.builder()
                .reportType(request.getReportType())
                .frequency(request.getFrequency())
                .formats(request.getFormats())
                .recipients(request.getRecipients())
                .nextExecutionDate(calculateNextExecution(request))
                .status(ScheduleStatus.ACTIVE)
                .build();

        return scheduleRepository.save(schedule);
    }

    /**
     * Executa relatórios agendados
     */
    @Scheduled(cron = "0 0 * * * *") // Executa a cada hora
    public void executeScheduledReports() {
        log.info("Iniciando execução de relatórios agendados");

        LocalDateTime now = LocalDateTime.now();

        List<ReportSchedule> schedulesToRun = scheduleRepository
                .findByStatusAndNextExecutionDateBefore(ScheduleStatus.ACTIVE, now);

        for (ReportSchedule schedule : schedulesToRun) {
            try {
                executeSchedule(schedule);
                updateNextExecution(schedule);
            } catch (Exception e) {
                log.error("Erro ao executar relatório agendado: {}", e.getMessage());
                handleScheduleError(schedule, e);
            }
        }
    }

    private void executeSchedule(ReportSchedule schedule) {
        // Criar request de exportação
        ReportExportRequest exportRequest = createExportRequest(schedule);

        // Executar exportação
        List<String> exportedFiles = exportService.exportReport(exportRequest);

        // Enviar para destinatários
        sendToRecipients(schedule, exportedFiles);

        // Registrar execução
        logExecution(schedule, exportedFiles);
    }

    private void updateNextExecution(ReportSchedule schedule) {
        LocalDateTime next = calculateNextExecution(schedule);
        schedule.setNextExecutionDate(next);
        schedule.setLastExecutionDate(LocalDateTime.now());
        scheduleRepository.save(schedule);
    }
}
