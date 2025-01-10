package Prism.Erp.service.impl;

import Prism.Erp.dto.InvoiceDTO;
import Prism.Erp.dto.InvoiceTaxCalculationDTO;
import Prism.Erp.entity.*;
import Prism.Erp.exception.BusinessException;
import Prism.Erp.exception.ResourceNotFoundException;
import Prism.Erp.model.InvoiceStatus;
import Prism.Erp.repository.InvoiceRepository;
import Prism.Erp.repository.SalesOrderRepository;
import Prism.Erp.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final SalesOrderRepository salesOrderRepository;

    private static final BigDecimal TAX_RATE_ICMS = new BigDecimal("0.18");
    private static final BigDecimal TAX_RATE_IPI = new BigDecimal("0.05");
    private static final BigDecimal TAX_RATE_PIS = new BigDecimal("0.0165");
    private static final BigDecimal TAX_RATE_COFINS = new BigDecimal("0.076");

    @Override
    @Transactional
    public InvoiceDTO generateInvoice(Long orderId) {
        SalesOrder order = salesOrderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));

        validateOrderForInvoice(order);

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(generateInvoiceNumber());
        invoice.setSalesOrder(order);
        invoice.setCustomer(order.getCustomer());
        invoice.setIssueDate(LocalDateTime.now());
        invoice.setDueDate(LocalDateTime.now().plusDays(30)); // Configurable
        invoice.setStatus(InvoiceStatus.PENDING);

        // Cálculo de valores e impostos
        InvoiceTaxCalculationDTO taxes = calculateTaxesForOrder(order);

        invoice.setSubtotal(order.getTotalAmount());
        invoice.setTaxAmount(taxes.getTotalTaxes());
        invoice.setTotalAmount(order.getTotalAmount().add(taxes.getTotalTaxes()));

        // Valores discriminados de impostos
        invoice.setIcmsValue(taxes.getIcmsValue());
        invoice.setIpiValue(taxes.getIpiValue());
        invoice.setPisValue(taxes.getPisValue());
        invoice.setCofinsValue(taxes.getCofinsValue());

        Invoice savedInvoice = invoiceRepository.save(invoice);
        return convertToDTO(savedInvoice);
    }

    @Override
    public InvoiceDTO getInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Fatura não encontrada"));
    }

    @Override
    @Transactional
    public InvoiceDTO updateStatus(Long id, InvoiceStatus status) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fatura não encontrada"));

        validateStatusTransition(invoice.getStatus(), status);
        invoice.setStatus(status);

        return convertToDTO(invoiceRepository.save(invoice));
    }

    @Override
    public Page<InvoiceDTO> getAllInvoices(Pageable pageable) {
        return invoiceRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    @Override
    public Page<InvoiceDTO> findInvoicesByStatus(InvoiceStatus status, Pageable pageable) {
        return invoiceRepository.findByStatus(status, pageable)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional
    public InvoiceDTO applyDiscount(Long id, BigDecimal discountPercentage) {
        if (discountPercentage.compareTo(BigDecimal.ZERO) < 0 ||
                discountPercentage.compareTo(new BigDecimal("100")) > 0) {
            throw new BusinessException("Percentual de desconto inválido");
        }

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fatura não encontrada"));

        if (invoice.getStatus() != InvoiceStatus.PENDING) {
            throw new BusinessException("Desconto só pode ser aplicado em faturas pendentes");
        }

        BigDecimal discountAmount = invoice.getTotalAmount()
                .multiply(discountPercentage)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        invoice.setDiscountAmount(discountAmount);
        invoice.setTotalAmount(invoice.getTotalAmount().subtract(discountAmount));

        return convertToDTO(invoiceRepository.save(invoice));
    }

    @Override
    public InvoiceTaxCalculationDTO calculateTaxes(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fatura não encontrada"));

        return calculateTaxesForOrder(invoice.getSalesOrder());
    }

    @Override
    public InvoiceDTO getByInvoiceNumber(String invoiceNumber) {
        return invoiceRepository.findByInvoiceNumber(invoiceNumber)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Fatura não encontrada"));
    }

    private InvoiceTaxCalculationDTO calculateTaxesForOrder(SalesOrder order) {
        BigDecimal baseValue = order.getTotalAmount();

        BigDecimal icmsValue = baseValue.multiply(TAX_RATE_ICMS);
        BigDecimal ipiValue = baseValue.multiply(TAX_RATE_IPI);
        BigDecimal pisValue = baseValue.multiply(TAX_RATE_PIS);
        BigDecimal cofinsValue = baseValue.multiply(TAX_RATE_COFINS);

        BigDecimal totalTaxes = icmsValue
                .add(ipiValue)
                .add(pisValue)
                .add(cofinsValue);

        return InvoiceTaxCalculationDTO.builder()
                .baseValue(baseValue)
                .icmsValue(icmsValue)
                .ipiValue(ipiValue)
                .pisValue(pisValue)
                .cofinsValue(cofinsValue)
                .totalTaxes(totalTaxes)
                .build();
    }

    private void validateOrderForInvoice(SalesOrder order) {
        if (order.getInvoice() != null) {
            throw new BusinessException("Pedido já possui fatura gerada");
        }

        // Adicione outras validações necessárias
        if (order.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Pedido com valor total inválido");
        }
    }

    private void validateStatusTransition(InvoiceStatus currentStatus, InvoiceStatus newStatus) {
        // Implemente as regras de transição de status
        if (currentStatus == InvoiceStatus.CANCELLED) {
            throw new BusinessException("Não é possível alterar o status de uma fatura cancelada");
        }

        if (currentStatus == InvoiceStatus.PAID && newStatus != InvoiceStatus.CANCELLED) {
            throw new BusinessException("Uma fatura paga só pode ser cancelada");
        }
    }

    private String generateInvoiceNumber() {
        return "NF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private InvoiceDTO convertToDTO(Invoice invoice) {
        return InvoiceDTO.builder()
                .id(invoice.getId())
                .invoiceNumber(invoice.getInvoiceNumber())
                .customerId(invoice.getCustomer().getId())
                .customerName(invoice.getCustomer().getName())
                .orderId(invoice.getSalesOrder().getId())
                .issueDate(invoice.getIssueDate())
                .dueDate(invoice.getDueDate())
                .subtotal(invoice.getSubtotal())
                .taxAmount(invoice.getTaxAmount())
                .discountAmount(invoice.getDiscountAmount())
                .totalAmount(invoice.getTotalAmount())
                .status(invoice.getStatus())
                .build();
    }
}