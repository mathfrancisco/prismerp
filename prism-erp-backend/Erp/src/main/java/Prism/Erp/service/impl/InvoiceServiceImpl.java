package Prism.Erp.service.impl;

import Prism.Erp.dto.InvoiceDTO;
import Prism.Erp.dto.InvoiceTaxCalculationDTO;
import Prism.Erp.entity.Invoice;
import Prism.Erp.entity.SalesOrder;
import Prism.Erp.exception.ResourceNotFoundException;
import Prism.Erp.model.InvoiceStatus;
import Prism.Erp.repository.InvoiceRepository;
import Prism.Erp.repository.SalesOrderRepository;
import Prism.Erp.service.InvoiceService;
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
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final SalesOrderRepository salesOrderRepository;

    private static final BigDecimal ICMS_RATE = new BigDecimal("0.18"); // 18%
    private static final BigDecimal PIS_RATE = new BigDecimal("0.0165"); // 1.65%
    private static final BigDecimal COFINS_RATE = new BigDecimal("0.076"); // 7.6%
    private static final BigDecimal ISS_RATE = new BigDecimal("0.05"); // 5%


    @Override
    @Transactional
    public InvoiceDTO generateInvoice(Long orderId) {
        SalesOrder salesOrder = salesOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido de venda não encontrado"));

        // Verifica se já existe fatura para este pedido
        invoiceRepository.findBySalesOrderId(orderId)
                .ifPresent(existingInvoice -> {
                    throw new RuntimeException("Já existe uma fatura para este pedido");
                });

        Invoice invoice = new Invoice();
        invoice.setSalesOrder(salesOrder);
        invoice.setInvoiceNumber(generateInvoiceNumber());
        invoice.setSubtotal(salesOrder.getTotalAmount()); // Set initial subtotal
        invoice.setTotalAmount(salesOrder.getTotalAmount());
        invoice.setDiscountPercentage(BigDecimal.ZERO); // Initialize with zero discount
        invoice.setStatus(InvoiceStatus.DRAFT);
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setDueDate(LocalDate.now().plusDays(30));

        return convertToDTO(invoiceRepository.save(invoice));
    }

    @Override
    public InvoiceDTO getInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Fatura não encontrada"));
    }

    @Override
    @Transactional
    public InvoiceDTO updateStatus(Long id, InvoiceStatus status) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fatura não encontrada"));

        validateStatusTransition(invoice.getStatus(), status);
        invoice.setStatus(status);

        return convertToDTO(invoiceRepository.save(invoice));
    }

    private String generateInvoiceNumber() {
        // Formato: INV-YYYYMMDD-XXXX onde XXXX é um número aleatório
        return "INV-" + LocalDate.now().toString().replace("-", "") + "-"
                + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }

    private void validateStatusTransition(InvoiceStatus currentStatus, InvoiceStatus newStatus) {
        // Implementa regras de transição de status
        if (currentStatus == InvoiceStatus.CANCELLED || currentStatus == InvoiceStatus.VOID) {
            throw new RuntimeException("Não é possível alterar o status de uma fatura cancelada ou anulada");
        }

        if (currentStatus == InvoiceStatus.PAID && newStatus != InvoiceStatus.VOID) {
            throw new RuntimeException("Uma fatura paga só pode ser anulada");
        }

        // Adicione outras regras de validação conforme necessário
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
            throw new IllegalArgumentException("Desconto deve estar entre 0 e 100");
        }

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fatura não encontrada"));

        if (invoice.getStatus() != InvoiceStatus.DRAFT) {
            throw new RuntimeException("Desconto só pode ser aplicado em faturas em rascunho");
        }

        BigDecimal discountMultiplier = BigDecimal.ONE.subtract(
                discountPercentage.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));

        invoice.setDiscountPercentage(discountPercentage);
        invoice.setTotalAmount(invoice.getSubtotal().multiply(discountMultiplier));

        return convertToDTO(invoiceRepository.save(invoice));
    }

    @Override
    public InvoiceTaxCalculationDTO calculateTaxes(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fatura não encontrada"));

        BigDecimal subtotal = invoice.getSubtotal();
        BigDecimal discountAmount = calculateDiscountAmount(invoice);
        BigDecimal baseForTaxes = subtotal.subtract(discountAmount);

        BigDecimal icmsAmount = baseForTaxes.multiply(ICMS_RATE);
        BigDecimal pisAmount = baseForTaxes.multiply(PIS_RATE);
        BigDecimal cofinsAmount = baseForTaxes.multiply(COFINS_RATE);
        BigDecimal issAmount = baseForTaxes.multiply(ISS_RATE);

        BigDecimal totalTaxes = icmsAmount
                .add(pisAmount)
                .add(cofinsAmount)
                .add(issAmount);

        BigDecimal totalAmount = baseForTaxes.add(totalTaxes);

        return InvoiceTaxCalculationDTO.builder()
                .subtotal(subtotal)
                .discountAmount(discountAmount)
                .taxAmount(totalTaxes)
                .totalAmount(totalAmount)
                .icmsAmount(icmsAmount)
                .pisAmount(pisAmount)
                .cofinsAmount(cofinsAmount)
                .issAmount(issAmount)
                .build();
    }

    private BigDecimal calculateDiscountAmount(Invoice invoice) {
        if (invoice.getDiscountPercentage() == null ||
                invoice.getDiscountPercentage().compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return invoice.getSubtotal()
                .multiply(invoice.getDiscountPercentage())
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }

    @Override
    public InvoiceDTO getByInvoiceNumber(String invoiceNumber) {
        return invoiceRepository.findByInvoiceNumber(invoiceNumber)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Fatura não encontrada com o número: " + invoiceNumber));
    }
}