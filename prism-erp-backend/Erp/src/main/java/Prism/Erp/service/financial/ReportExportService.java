package Prism.Erp.service.financial;

import Prism.Erp.dto.financial.DREReport;
import Prism.Erp.dto.financial.ReportExportRequest;
import Prism.Erp.model.financial.ExportFormat;
import Prism.Erp.service.NotificationService;
import Prism.Erp.service.UserService;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ReportExportService {

    @Autowired
    private FinancialReportService reportService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Value("${reports.export.path}")
    private String exportBasePath;

    /**
     * Exporta relatório em vários formatos
     */
    public List<String> exportReport(ReportExportRequest request) {
        log.info("Exportando relatório: {} em formatos: {}",
                request.getReportType(), request.getFormats());

        List<String> exportedFiles = new ArrayList<>();

        try {
            // Gerar o relatório
            Object report = generateReport(request);

            // Exportar em cada formato solicitado
            for (ExportFormat format : request.getFormats()) {
                String filePath = exportToFormat(report, format, request);
                exportedFiles.add(filePath);
            }

            // Notificar usuário sobre exportação concluída
            notifyExportComplete(request, exportedFiles);

            return exportedFiles;

        } catch (Exception e) {
            log.error("Erro ao exportar relatório: {}", e.getMessage());
            notifyExportError(request, e);
            throw new ReportExportException("Falha na exportação do relatório", e);
        }
    }

    private Object generateReport(ReportExportRequest request) {
        switch (request.getReportType()) {
            case DRE:
                return reportService.generateDRE(request.getStartDate(), request.getEndDate());
            case CASH_FLOW:
                return reportService.generateCashFlow(request.getStartDate(), request.getEndDate());
            case COST_CENTER:
                return reportService.generateCostCenterReport(
                        request.getCostCenterId(),
                        request.getStartDate(),
                        request.getEndDate()
                );
            case ACCOUNTS_PAYABLE:
                return reportService.generateAccountsPayableReport(request.getReferenceDate());
            default:
                throw new IllegalArgumentException("Tipo de relatório não suportado");
        }
    }

    private String exportToFormat(Object report, ExportFormat format, ReportExportRequest request) {
        String fileName = generateFileName(request, format);
        String filePath = exportBasePath + "/" + fileName;

        switch (format) {
            case PDF:
                return exportToPdf(report, filePath);
            case EXCEL:
                return exportToExcel(report, filePath);
            case CSV:
                return exportToCsv(report, filePath);
            default:
                throw new IllegalArgumentException("Formato não suportado");
        }
    }

    private String exportToPdf(Object report, String filePath) {
        try (PDFGenerator pdf = new PDFGenerator(filePath)) {
            if (report instanceof DREReport) {
                exportDREtoPdf((DREReport) report, pdf);
            } else if (report instanceof CashFlowReport) {
                exportCashFlowToPdf((CashFlowReport) report, pdf);
            }
            // ... outros tipos de relatório

            return filePath;
        }
    }

    private String exportToExcel(Object report, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            if (report instanceof DREReport) {
                exportDREtoExcel((DREReport) report, workbook);
            } else if (report instanceof CashFlowReport) {
                exportCashFlowToExcel((CashFlowReport) report, workbook);
            }
            // ... outros tipos de relatório

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }

            return filePath;
        }
    }
}