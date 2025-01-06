package Prism.Erp.service.impl;

import Prism.Erp.dto.InvoiceDTO;
import Prism.Erp.entity.Invoice;
import Prism.Erp.service.AbstractCrudService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceServiceImpl extends AbstractCrudService<Invoice, InvoiceDTO> {
    private final SalesOrderRepository salesOrderRepository;

    @Override
    protected InvoiceDTO toDTO(Invoice entity) {
        return InvoiceDTO.builder()
                .id(entity.getId())
                .invoiceNumber(entity.getInvoiceNumber())
                .salesOrderId(entity.getSalesOrder().getId())
                .totalAmount(entity.getTotalAmount())
                .status(entity.getStatus().name())
                .invoiceDate(entity.getInvoiceDate())
                .dueDate(entity.getDueDate())
                .build();
    }

    @Override
    protected String getEntityName() {
        return "Invoice";
    }
}
