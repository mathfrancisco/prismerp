package Prism.Erp.config.business.audit;

import Prism.Erp.entity.business.Audit.AuditLogDTO;
import Prism.Erp.entity.business.Audit.AuditSearchCriteria;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
@Validated
public class AuditController {

    private final AuditService auditService;

    @GetMapping("/search")
    public ResponseEntity<Page<AuditLogDTO>> searchAuditLogs(
            @Valid AuditSearchCriteria criteria,
            Pageable pageable) {
        return ResponseEntity.ok(auditService.searchAuditLogs(criteria, pageable));
    }

    @GetMapping("/entity/{type}/{id}")
    public ResponseEntity<List<AuditLogDTO>> getEntityHistory(
            @PathVariable String type,
            @PathVariable String id) {
        return ResponseEntity.ok(auditService.getEntityHistory(type, id));
    }

    @GetMapping("/export")
    public ResponseEntity<Resource> exportAuditLogs(
            @Valid AuditSearchCriteria criteria,
            @RequestParam String format) {
        Resource report = auditService.exportAuditLogs(criteria, format);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=audit-report." + format.toLowerCase())
                .body(report);
    }
}
