package Prism.Erp.controller.business;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@Slf4j
public class BusinessDashboardController {
    private final BusinessDashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<BusinessDashboardDTO> getDashboardSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(dashboardService.generateDashboardSummary(startDate, endDate));
    }

    @GetMapping("/charts")
    public ResponseEntity<List<ChartDataDTO>> getChartData(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String chartType) {
        return ResponseEntity.ok(dashboardService.generateChartData(startDate, endDate, chartType));
    }

    @GetMapping("/deviations")
    public ResponseEntity<List<DeviationDTO>> getDeviations() {
        return ResponseEntity.ok(dashboardService.findSignificantDeviations());
    }

    @GetMapping("/delays")
    public ResponseEntity<List<DelayDTO>> getDelays() {
        return ResponseEntity.ok(dashboardService.findCurrentDelays());
    }

    @GetMapping("/export")
    public ResponseEntity<Resource> exportDashboard(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "PDF") String format) {
        Resource report = dashboardService.exportDashboard(startDate, endDate, format);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, 
                   "attachment; filename=dashboard-report." + format.toLowerCase())
            .body(report);
    }
}
