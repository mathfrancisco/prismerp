package Prism.Erp.service.impl;

import Prism.Erp.dto.*;
import Prism.Erp.dto.financial.FinancialTransactionDTO;
import Prism.Erp.exception.BusinessException;
import Prism.Erp.model.PaymentFrequency;
import Prism.Erp.service.InvoiceService;
import Prism.Erp.service.NotificationService;
import Prism.Erp.service.SaleService;
import Prism.Erp.service.SalesFinancialIntegrationService;
import Prism.Erp.service.business.integration.FinancialIntegrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.beanvalidation.IntegrationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalesFinancialIntegrationServiceImpl implements SalesFinancialIntegrationService {

    private final FinancialIntegrationService financialService;
    private final SaleService saleService;
    private final InvoiceService invoiceService;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public void processSaleFinancialIntegration(Long saleId) {
        try {
            // 1. Buscar dados da venda
            SaleDTO sale = saleService.getSale(saleId);
            if (sale == null) {
                throw new BusinessException("Venda não encontrada");
            }

            // 2. Gerar fatura
            InvoiceDTO invoice = invoiceService.generateInvoice(saleId);

            // 3. Criar conta a receber
            AccountReceivableDTO receivable = financialService.createReceivableFromInvoice(invoice.getId());

            // 4. Criar plano de pagamento se necessário
            if (sale.getPaymentTerms() != null) {
                PaymentPlanDTO planDTO = createPaymentPlan(sale, invoice);
                financialService.createPaymentPlan(invoice.getId(), planDTO);
            }

            // 5. Atualizar status da venda
            saleService.updateSaleFinancialStatus(saleId, FinancialStatus.PROCESSED);

            // 6. Notificar interessados
            notifyFinancialProcessing(sale, invoice, receivable);

        } catch (Exception e) {
            log.error("Erro ao processar integração financeira da venda {}: {}", saleId, e.getMessage());
            throw new IntegrationException("Falha na integração financeira", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public FinancialSaleStatusDTO checkSaleFinancialStatus(Long saleId) {
        SaleDTO sale = saleService.getSale(saleId);
        List<AccountReceivableDTO> receivables = financialService.getReceivablesBySale(saleId);
        
        return FinancialSaleStatusDTO.builder()
            .saleId(saleId)
            .totalAmount(sale.getTotalAmount())
            .receivedAmount(calculateReceivedAmount(receivables))
            .pendingAmount(calculatePendingAmount(receivables))
            .status(determineFinancialStatus(receivables))
            .lastPaymentDate(findLastPaymentDate(receivables))
            .nextDueDate(findNextDueDate(receivables))
            .build();
    }

    @Override
    @Transactional
    public void processPayment(Long saleId, PaymentDTO payment) {
        // 1. Validar pagamento
        validatePayment(saleId, payment);

        // 2. Registrar transação financeira
        FinancialTransactionDTO transaction = financialService.registerPayment(
            payment.getReceivableId(), 
            createTransactionDTO(payment)
        );

        // 3. Atualizar status se necessário
        updateSaleStatusAfterPayment(saleId);
    }

    private PaymentPlanDTO createPaymentPlan(SaleDTO sale, InvoiceDTO invoice) {
        return PaymentPlanDTO.builder()
            .invoiceId(invoice.getId())
            .numberOfInstallments(sale.getPaymentTerms().getInstallments())
            .installmentAmount(calculateInstallmentAmount(sale))
            .frequency(PaymentFrequency.MONTHLY)
            .firstDueDate(sale.getPaymentTerms().getFirstDueDate())
            .interestRate(sale.getPaymentTerms().getInterestRate())
            .paymentMethod(sale.getPaymentTerms().getPaymentMethod())
            .build();
    }

    private void notifyFinancialProcessing(SaleDTO sale, InvoiceDTO invoice, AccountReceivableDTO receivable) {
        NotificationDTO notification = NotificationDTO.builder()
            .type(NotificationType.FINANCIAL_INTEGRATION)
            .subject("Integração Financeira - Venda " + sale.getId())
            .message(buildNotificationMessage(sale, invoice, receivable))
            .recipients(getFinancialNotificationRecipients())
            .build();

        notificationService.sendNotification(notification);
    }

    private void validatePayment(Long saleId, PaymentDTO payment) {
        // Implementar validações específicas do pagamento
        // Ex: valor não pode exceder saldo, data válida, etc.
    }

    private void updateSaleStatusAfterPayment(Long saleId) {
        FinancialSaleStatusDTO status = checkSaleFinancialStatus(saleId);
        if (status.getPendingAmount().compareTo(BigDecimal.ZERO) == 0) {
            saleService.updateSaleFinancialStatus(saleId, FinancialStatus.PAID);
        }
    }
}
