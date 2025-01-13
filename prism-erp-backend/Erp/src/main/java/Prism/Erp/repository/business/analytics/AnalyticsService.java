package Prism.Erp.repository.business.analytics;

import Prism.Erp.entity.business.analytics.AnalyticsReport;
import Prism.Erp.model.business.ReportType;
import Prism.Erp.repository.business.purchase.PurchaseOrderRepository;
import Prism.Erp.repository.business.supplier.SupplierRepository;
import Prism.Erp.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AnalyticsService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final InventoryMovementRepository inventoryMovementRepository;
    private final SupplierRepository supplierRepository;
    private final AuditLogRepository auditLogRepository;
    private final AnalyticsReportRepository reportRepository;
    private final NotificationService notificationService;

    @Cacheable(value = "analyticsCache", key = "#type + #startDate + #endDate")
    public AnalyticsReportDTO generateReport(
            ReportType type,
            LocalDate startDate,
            LocalDate endDate,
            Map<String, Object> parameters) {

        AnalyticsReport report = new AnalyticsReport();
        report.setReportName(type.name());
        report.setType(type);
        report.setParameters(parameters);
        report.setGeneratedAt(LocalDateTime.now());
        report.setGeneratedBy(SecurityContextHolder.getContext().getAuthentication().getName());

        Map<String, Object> reportData = new HashMap<>();

        switch (type) {
            case PURCHASE_ANALYSIS:
                reportData = generatePurchaseAnalysis(startDate, endDate);
                break;
            case INVENTORY_ANALYSIS:
                reportData = generateInventoryAnalysis(startDate, endDate);
                break;
            case SUPPLIER_PERFORMANCE:
                reportData = generateSupplierPerformance(startDate, endDate);
                break;
            case AUDIT_SUMMARY:
                reportData = generateAuditSummary(startDate, endDate);
                break;
            default:
                throw new UnsupportedReportTypeException(type);
        }

        report.setData(new ObjectMapper().writeValueAsString(reportData));
        reportRepository.save(report);

        analyzeForAnomalies(reportData, type);

        return modelMapper.map(report, AnalyticsReportDTO.class);
    }

    private Map<String, Object> generatePurchaseAnalysis(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> analysis = new HashMap<>();

        // Análise de Compras
        analysis.put("totalOrders", purchaseOrderRepository.countByDateBetween(startDate, endDate));
        analysis.put("totalValue", purchaseOrderRepository.sumTotalValueByDateBetween(startDate, endDate));
        analysis.put("averageOrderValue", purchaseOrderRepository.averageOrderValueByDateBetween(startDate, endDate));
        analysis.put("ordersByStatus", purchaseOrderRepository.countByStatusAndDateBetween(startDate, endDate));
        analysis.put("topSuppliers", purchaseOrderRepository.findTopSuppliersByValue(startDate, endDate, 10));

        // Tendências
        analysis.put("monthlyTrend", generateMonthlyTrend(startDate, endDate));
        analysis.put("categoryDistribution", purchaseOrderRepository.findCategoryDistribution(startDate, endDate));

        return analysis;
    }

    private Map<String, Object> generateInventoryAnalysis(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> analysis = new HashMap<>();

        // Análise de Estoque
        analysis.put("totalItems", inventoryMovementRepository.countDistinctItems());
        analysis.put("lowStockItems", inventoryMovementRepository.findLowStockItems());
        analysis.put("topMovingItems", inventoryMovementRepository.findTopMovingItems(startDate, endDate, 10));
        analysis.put("stockTurnover", calculateStockTurnover(startDate, endDate));

        // Movimentações
        analysis.put("movementsByType", inventoryMovementRepository.countByTypeAndDateBetween(startDate, endDate));
        analysis.put("valueByCategory", inventoryMovementRepository.sumValueByCategory(startDate, endDate));

        return analysis;
    }

    private Map<String, Object> generateSupplierPerformance(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> analysis = new HashMap<>();

        // Performance de Fornecedores
        analysis.put("deliveryPerformance", calculateDeliveryPerformance(startDate, endDate));
        analysis.put("qualityMetrics", calculateQualityMetrics(startDate, endDate));
        analysis.put("costSavings", calculateCostSavings(startDate, endDate));
        analysis.put("supplierRankings", generateSupplierRankings(startDate, endDate));

        return analysis;
    }

    private void analyzeForAnomalies(Map<String, Object> reportData, ReportType type) {
        List<Anomaly> anomalies = anomalyDetectionService.detectAnomalies(reportData, type);

        for (Anomaly anomaly : anomalies) {
            if (anomaly.getSeverity().isHigh()) {
                notificationService.createAnalyticsAlert(
                        anomaly.getDescription(),
                        NotificationPriority.HIGH,
                        anomaly.getMetadata()
                );
            }
        }
    }
}
