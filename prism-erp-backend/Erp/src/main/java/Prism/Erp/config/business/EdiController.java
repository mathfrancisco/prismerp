package Prism.Erp.config.business;

import Prism.Erp.dto.business.edi.EdiTransactionDTO;
import Prism.Erp.model.business.EdiStatus;
import Prism.Erp.model.business.EdiTransactionType;
import Prism.Erp.service.business.edi.EdiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/edi")
@RequiredArgsConstructor
@Validated
public class EdiController {

    private final EdiService ediService;
    private final EdiConfigurationService configService;

    @PostMapping("/configurations")
    @PreAuthorize("hasRole('EDI_ADMIN')")
    public ResponseEntity<EdiConfigurationDTO> createConfiguration(
            @Valid @RequestBody CreateEdiConfigurationDTO createDTO) {
        return ResponseEntity.ok(configService.createConfiguration(createDTO));
    }

    @PostMapping("/inbound")
    public ResponseEntity<Void> handleInboundMessage(
            @RequestHeader("X-Supplier-ID") String supplierId,
            @RequestHeader("X-Transaction-Type") EdiTransactionType type,
            @RequestBody String rawMessage) {
        ediService.processInboundMessage(supplierId, rawMessage, type);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/transactions")
    @PreAuthorize("hasRole('EDI_ADMIN')")
    public ResponseEntity<Page<EdiTransactionDTO>> getTransactions(
            @RequestParam(required = false) String supplierId,
            @RequestParam(required = false) EdiStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Pageable pageable) {
        return ResponseEntity.ok(configService.getTransactions(supplierId, status, startDate, endDate, pageable));
    }
}
