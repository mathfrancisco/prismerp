package Prism.Erp.service.impl;

import Prism.Erp.dto.InvoiceDTO;
import Prism.Erp.entity.Invoice;
import Prism.Erp.entity.SalesOrder;
import Prism.Erp.model.InvoiceStatus;
import Prism.Erp.repository.InvoiceRepository;
import Prism.Erp.repository.SalesOrderRepository;
import Prism.Erp.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final SalesOrderRepository salesOrderRepository;

    @Override
    public InvoiceDTO generateInvoice(Long orderId) {
        SalesOrder salesOrder = salesOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido de venda não encontrado")); // Exceção mais específica

        // Lógica para gerar a fatura com base no pedido de venda
        // ...

        Invoice invoice = new Invoice(); // Crie e preencha a entidade Invoice
        return convertToDTO(invoiceRepository.save(invoice));
    }

    @Override
    public InvoiceDTO getInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Fatura não encontrada")); // Exceção mais específica
    }

    @Override
    public InvoiceDTO updateStatus(Long id, InvoiceStatus status) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fatura não encontrada")); // Exceção mais específica

        invoice.setStatus(status);
        return convertToDTO(invoiceRepository.save(invoice));
    }


    private InvoiceDTO convertToDTO(Invoice entity) {
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

    // Implemente o método convertToEntity se necessário
    // ...
}
