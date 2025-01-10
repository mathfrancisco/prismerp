package Prism.Erp.service.impl;

import Prism.Erp.dto.*;
import Prism.Erp.entity.*;
import Prism.Erp.exception.BusinessException;
import Prism.Erp.model.*;
import Prism.Erp.repository.*;
import Prism.Erp.service.FinancialIntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FinancialIntegrationServiceImpl implements FinancialIntegrationService {

    private final AccountReceivableRepository accountReceivableRepository;
    private final FinancialTransactionRepository transactionRepository;
    private final PaymentPlanRepository paymentPlanRepository;
    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public AccountReceivableDTO createReceivableFromInvoice(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new BusinessException("Fatura não encontrada"));

        // Validação de estado da fatura
        if (invoice.getStatus() != InvoiceStatus.ISSUED) {
            throw new BusinessException("Fatura deve estar emitida para gerar contas a receber");
        }

        // Verifica se já existe conta a receber para esta fatura
        if (accountReceivableRepository.existsByInvoiceId(invoiceId)) {
            throw new BusinessException("Já existe conta a receber para esta fatura");
        }

        AccountReceivable receivable = new AccountReceivable();
        receivable.setDocumentNumber(generateDocumentNumber());
        receivable.setCustomer(invoice.getSalesOrder().getCustomer());
        receivable.setInvoice(invoice);
        receivable.setAmount(invoice.getTotalAmount());
        receivable.setDueDate(invoice.getDueDate());
        receivable.setStatus(ReceivableStatus.PENDING);
        receivable.setRemainingAmount(invoice.getTotalAmount());

        return convertToDTO(accountReceivableRepository.save(receivable));
    }

    @Override
    @Transactional
    public PaymentPlanDTO createPaymentPlan(Long invoiceId, PaymentPlanDTO planDTO) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new BusinessException("Fatura não encontrada"));

        // Validações de negócio
        if (planDTO.getNumberOfInstallments() <= 0) {
            throw new BusinessException("Número de parcelas deve ser maior que zero");
        }

        if (planDTO.getInstallmentAmount().multiply(
                        BigDecimal.valueOf(planDTO.getNumberOfInstallments()))
                .compareTo(invoice.getTotalAmount()) != 0) {
            throw new BusinessException("Valor total das parcelas não corresponde ao valor da fatura");
        }

        PaymentPlan plan = new PaymentPlan();
        plan.setInvoice(invoice);
        plan.setNumberOfInstallments(planDTO.getNumberOfInstallments());
        plan.setInstallmentAmount(planDTO.getInstallmentAmount());
        plan.setFrequency(PaymentFrequency.valueOf(planDTO.getFrequency()));
        plan.setFirstDueDate(planDTO.getFirstDueDate());
        plan.setInterestRate(planDTO.getInterestRate());
        plan.setPaymentMethod(planDTO.getPaymentMethod());

        return convertToDTO(paymentPlanRepository.save(plan));
    }

    @Override
    @Transactional
    public FinancialTransactionDTO registerPayment(Long receivableId, FinancialTransactionDTO paymentDTO) {
        AccountReceivable receivable = accountReceivableRepository.findById(receivableId)
                .orElseThrow(() -> new BusinessException("Conta a receber não encontrada"));

        // Validações de negócio
        if (receivable.getStatus() == ReceivableStatus.PAID) {
            throw new BusinessException("Conta já está paga");
        }

        if (paymentDTO.getAmount().compareTo(receivable.getRemainingAmount()) > 0) {
            throw new BusinessException("Valor do pagamento maior que o saldo restante");
        }

        // Registra a transação
        FinancialTransaction transaction = new FinancialTransaction();
        transaction.setTransactionNumber(generateTransactionNumber());
        transaction.setType(TransactionType.PAYMENT);
        transaction.setAccountReceivable(receivable);
        transaction.setAmount(paymentDTO.getAmount());
        transaction.setTransactionDate(LocalDate.now());
        transaction.setPaymentMethod(paymentDTO.getPaymentMethod());
        transaction.setBankAccount(paymentDTO.getBankAccount());
        transaction.setDescription("Pagamento referente à fatura " + receivable.getInvoice().getInvoiceNumber());

        // Atualiza o status da conta a receber
        BigDecimal newRemainingAmount = receivable.getRemainingAmount().subtract(paymentDTO.getAmount());
        receivable.setRemainingAmount(newRemainingAmount);
        receivable.setPaidAmount(receivable.getPaidAmount().add(paymentDTO.getAmount()));

        if (newRemainingAmount.compareTo(BigDecimal.ZERO) == 0) {
            receivable.setStatus(ReceivableStatus.PAID);
            receivable.setPaymentDate(LocalDate.now());

            // Atualiza status da fatura
            Invoice invoice = receivable.getInvoice();
            invoice.setStatus(InvoiceStatus.PAID);
            invoiceRepository.save(invoice);
        }

        accountReceivableRepository.save(receivable);
        return convertToDTO(transactionRepository.save(transaction));
    }

    @Override
    public FinancialSummaryDTO getCustomerFinancialSummary(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado"));

        List<AccountReceivable> receivables = accountReceivableRepository.findByCustomerId(customerId);

        BigDecimal totalReceivables = BigDecimal.ZERO;
        BigDecimal totalOverdue = BigDecimal.ZERO;
        BigDecimal totalPaid = BigDecimal.ZERO;
        BigDecimal totalPending = BigDecimal.ZERO;
        int overdueCount = 0;
        int pendingCount = 0;

        LocalDate today = LocalDate.now();

        for (AccountReceivable receivable : receivables) {
            totalReceivables = totalReceivables.add(receivable.getAmount());

            if (receivable.getStatus() == ReceivableStatus.PAID) {
                totalPaid = totalPaid.add(receivable.getAmount());
            } else {
                totalPending = totalPending.add(receivable.getRemainingAmount());
                pendingCount++;

                if (receivable.getDueDate().isBefore(today)) {
                    totalOverdue = totalOverdue.add(receivable.getRemainingAmount());
                    overdueCount++;
                }
            }
        }

        return FinancialSummaryDTO.builder()
                .totalReceivables(totalReceivables)
                .totalOverdue(totalOverdue)
                .totalPaid(totalPaid)
                .totalPending(totalPending)
                .overdueCount(overdueCount)
                .pendingCount(pendingCount)
                .build();
    }

    private String generateDocumentNumber() {
        return "REC-" + LocalDate.now().toString().replace("-", "") + "-" +
                UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }

    private String generateTransactionNumber() {
        return "TRX-" + LocalDate.now().toString().replace("-", "") + "-" +
                UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }

    private AccountReceivableDTO convertToDTO(AccountReceivable entity) {
        return AccountReceivableDTO.builder()
                .id(entity.getId())
                .documentNumber(entity.getDocumentNumber())
                .customerId(entity.getCustomer().getId())
                .customerName(entity.getCustomer().getName())
                .invoiceId(entity.getInvoice().getId())
                .amount(entity.getAmount())
                .dueDate(entity.getDueDate())
                .status(entity.getStatus().name())
                .paymentDate(entity.getPaymentDate())
                .paidAmount(entity.getPaidAmount())
                .remainingAmount(entity.getRemainingAmount())
                .paymentMethod(entity.getPaymentMethod())
                .build();
    }

    private PaymentPlanDTO convertToDTO(PaymentPlan entity) {
        return PaymentPlanDTO.builder()
                .id(entity.getId())
                .invoiceId(entity.getInvoice().getId())
                .numberOfInstallments(entity.getNumberOfInstallments())
                .installmentAmount(entity.getInstallmentAmount())
                .frequency(entity.getFrequency().name())
                .firstDueDate(entity.getFirstDueDate())
                .interestRate(entity.getInterestRate())
                .paymentMethod(entity.getPaymentMethod())
                .build();
    }

    private FinancialTransactionDTO convertToDTO(FinancialTransaction entity) {
        return FinancialTransactionDTO.builder()
                .id(entity.getId())
                .transactionNumber(entity.getTransactionNumber())
                .type(entity.getType().name())
                .accountReceivableId(entity.getAccountReceivable().getId())
                .amount(entity.getAmount())
                .transactionDate(entity.getTransactionDate())
                .description(entity.getDescription())
                .paymentMethod(entity.getPaymentMethod())
                .bankAccount(entity.getBankAccount())
                .build();
    }
}