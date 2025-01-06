package Prism.Erp.service;

import Prism.Erp.dto.InvoiceDTO;
import Prism.Erp.dto.InvoiceTaxCalculationDTO;
import Prism.Erp.model.InvoiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface InvoiceService {
    InvoiceDTO generateInvoice(Long orderId);
    InvoiceDTO getInvoiceById(Long id);
    InvoiceDTO updateStatus(Long id, InvoiceStatus status);

    Page<InvoiceDTO> getAllInvoices(Pageable pageable);

    Page<InvoiceDTO> findInvoicesByStatus(InvoiceStatus status, Pageable pageable);

    @Transactional
    InvoiceDTO applyDiscount(Long id, BigDecimal discountPercentage);

    InvoiceTaxCalculationDTO calculateTaxes(Long id);
}
