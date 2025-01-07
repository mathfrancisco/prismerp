package Prism.Erp.controller;

import Prism.Erp.dto.InvoiceDTO;
import Prism.Erp.dto.InvoiceTaxCalculationDTO;
import Prism.Erp.model.InvoiceStatus;
import Prism.Erp.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping("/generate/{orderId}")
    public ResponseEntity<InvoiceDTO> generateInvoice(@PathVariable Long orderId) {
        return ResponseEntity.ok(invoiceService.generateInvoice(orderId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDTO> getInvoice(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.getInvoiceById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<InvoiceDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam InvoiceStatus status) {
        return ResponseEntity.ok(invoiceService.updateStatus(id, status));
    }
    @GetMapping
    public ResponseEntity<Page<InvoiceDTO>> getAllInvoices(Pageable pageable) {
        return ResponseEntity.ok(invoiceService.getAllInvoices(pageable));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<InvoiceDTO>> getInvoicesByStatus(
            @PathVariable InvoiceStatus status,
            Pageable pageable) {
        return ResponseEntity.ok(invoiceService.findInvoicesByStatus(status, pageable));
    }

    @PostMapping("/{id}/discount")
    public ResponseEntity<InvoiceDTO> applyDiscount(
            @PathVariable Long id,
            @RequestParam BigDecimal discountPercentage) {
        return ResponseEntity.ok(invoiceService.applyDiscount(id, discountPercentage));
    }

    @GetMapping("/{id}/taxes")
    public ResponseEntity<InvoiceTaxCalculationDTO> calculateTaxes(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.calculateTaxes(id));
    }
    @GetMapping("/number/{invoiceNumber}")
    public ResponseEntity<InvoiceDTO> getByInvoiceNumber(@PathVariable String invoiceNumber) {
        return ResponseEntity.ok(invoiceService.getByInvoiceNumber(invoiceNumber));
    }
}
