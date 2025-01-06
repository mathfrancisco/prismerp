package Prism.Erp.service;

import Prism.Erp.dto.InvoiceDTO;
import Prism.Erp.model.InvoiceStatus;

public interface InvoiceService {
    InvoiceDTO generateInvoice(Long orderId);
    InvoiceDTO getInvoiceById(Long id);
    InvoiceDTO updateStatus(Long id, InvoiceStatus status);
}
