package Prism.Erp.controller.business;

import Prism.Erp.model.business.AnalyticsPeriod;
import Prism.Erp.model.business.ReportType;
import Prism.Erp.repository.business.analytics.AnalyticsService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.support.MetricType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
@Validated
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/reports")
    public ResponseEntity<AnalyticsReportDTO> generateReport(
            @RequestParam ReportType type,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Map<String, Object> parameters) {
        return ResponseEntity.ok(analyticsService.generateReport(type, startDate, endDate, parameters));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardDTO> getDashboard(
            @RequestParam AnalyticsPeriod period) {
        return ResponseEntity.ok(analyticsService.generateDashboard(period));
    }

    @GetMapping("/trends")
    public ResponseEntity<List<TrendDTO>> getTrends(
            @RequestParam MetricType metricType,
            @RequestParam AnalyticsPeriod period) {
        return ResponseEntity.ok(analyticsService.analyzeTrends(metricType, period));
    }

    @GetMapping("/export")
    public ResponseEntity<Resource> exportReport(
            @RequestParam ReportType type,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam String format) {
        Resource report = analyticsService.exportReport(type, startDate, endDate, format);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=analytics-report." + format.toLowerCase())
                .body(report);
    }
}