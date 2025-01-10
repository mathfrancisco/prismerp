package Prism.Erp.service.impl;

import Prism.Erp.dto.InvoiceDTO;
import Prism.Erp.dto.InvoiceTaxCalculationDTO;
import Prism.Erp.dto.SalesOrderDTO;
import Prism.Erp.entity.*;
import Prism.Erp.exception.BusinessException;
import Prism.Erp.exception.ResourceNotFoundException;
import Prism.Erp.model.InvoiceStatus;
import Prism.Erp.repository.InvoiceRepository;
import Prism.Erp.service.InvoiceService;
import Prism.Erp.service.SalesOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final SalesOrderService salesOrderService;

    private static final BigDecimal TAX_RATE_ICMS = new BigDecimal("0.18");
    private static final BigDecimal TAX_RATE_IPI = new BigDecimal("0.05");
    private static final BigDecimal TAX_RATE_PIS = new BigDecimal("0.0165");
    private static final BigDecimal TAX_RATE_COFINS = new BigDecimal("0.076");

    @Override
    @Transactional
    public InvoiceDTO generateInvoice(Long orderId) {
        SalesOrderDTO orderDTO = salesOrderService.getOrderById(orderId);
        SalesOrder order = toSalesOrder(orderDTO);

        validateOrderForInvoice(order);

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(generateInvoiceNumber());
        invoice.setSalesOrder(order);
        invoice.setStatus(InvoiceStatus.DRAFT);
        invoice.setTotalAmount(order.getTotalAmount());
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setDueDate(LocalDate.now().plusDays(30));

        // Cálculo de valores e impostos
        InvoiceTaxCalculationDTO taxes = calculateTaxesForOrder(order);
        BigDecimal taxAmount = calculateTax(order);

        invoice.setSubtotal(order.getTotalAmount());
        invoice.setTotalAmount(order.getTotalAmount().add(taxAmount));

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

        if (invoice.getStatus() != InvoiceStatus.DRAFT) {
            throw new BusinessException("Desconto só pode ser aplicado em faturas em rascunho");
        }

        BigDecimal discountAmount = invoice.getTotalAmount()
                .multiply(discountPercentage)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        invoice.setDiscountPercentage(discountPercentage);
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
    public BigDecimal calculateTax(SalesOrder salesOrder) {
        BigDecimal baseValue = salesOrder.getTotalAmount();

        return baseValue.multiply(TAX_RATE_ICMS)
                .add(baseValue.multiply(TAX_RATE_IPI))
                .add(baseValue.multiply(TAX_RATE_PIS))
                .add(baseValue.multiply(TAX_RATE_COFINS))
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public InvoiceDTO getByInvoiceNumber(String invoiceNumber) {
        return invoiceRepository.findByInvoiceNumber(invoiceNumber)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Fatura não encontrada"));
    }

    private InvoiceTaxCalculationDTO calculateTaxesForOrder(SalesOrder order) {
        BigDecimal baseValue = order.getTotalAmount();

        BigDecimal icmsAmount = baseValue.multiply(TAX_RATE_ICMS);
        BigDecimal pisAmount = baseValue.multiply(TAX_RATE_PIS);
        BigDecimal cofinsAmount = baseValue.multiply(TAX_RATE_COFINS);
        BigDecimal taxAmount = icmsAmount.add(pisAmount).add(cofinsAmount);

        return InvoiceTaxCalculationDTO.builder()
                .subtotal(baseValue)
                .icmsAmount(icmsAmount)
                .pisAmount(pisAmount)
                .cofinsAmount(cofinsAmount)
                .taxAmount(taxAmount)
                .totalAmount(baseValue.add(taxAmount))
                .build();
    }

    private void validateOrderForInvoice(SalesOrder order) {
        if (invoiceRepository.findBySalesOrderId(order.getId()).isPresent()) {
            throw new BusinessException("Pedido já possui fatura gerada");
        }

        if (order.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Pedido com valor total inválido");
        }
    }

    private void validateStatusTransition(InvoiceStatus currentStatus, InvoiceStatus newStatus) {
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
                .salesOrderId(invoice.getSalesOrder().getId())
                .totalAmount(invoice.getTotalAmount())
                .status(invoice.getStatus().toString())
                .invoiceDate(invoice.getInvoiceDate())
                .dueDate(invoice.getDueDate())
                .build();
    }

    private SalesOrder toSalesOrder(SalesOrderDTO orderDTO) {
        SalesOrder order = new SalesOrder();
        order.setId(orderDTO.getId());
        order.setOrderNumber(orderDTO.getOrderNumber());
        order.setTotalAmount(orderDTO.getTotalAmount());

        // Convert items if needed
        if (orderDTO.getItems() != null) {
            order.setItems(orderDTO.getItems().stream()
                    .map(itemDTO -> {
                        SalesOrderItem item = new SalesOrderItem();
                        item.setId(itemDTO.getId());
                        item.setQuantity(itemDTO.getQuantity());
                        item.setUnitPrice(itemDTO.getUnitPrice());
                        item.setTotalPrice(itemDTO.getTotalPrice());
                        return item;
                    })
                    .toList());
        }

        return order;
    }
}