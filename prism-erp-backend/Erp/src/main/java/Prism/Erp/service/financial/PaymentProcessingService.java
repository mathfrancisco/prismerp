package Prism.Erp.service.financial;

import Prism.Erp.dto.financial.PaymentRequest;
import Prism.Erp.dto.financial.PaymentResponse;
import Prism.Erp.entity.financial.AccountPayable;
import Prism.Erp.entity.financial.PaymentSchedule;
import Prism.Erp.exception.BusinessException;
import Prism.Erp.exception.ResourceNotFoundException;
import Prism.Erp.model.financial.PaymentStatus;
import Prism.Erp.repository.financial.AccountPayableRepository;
import Prism.Erp.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class PaymentProcessingService {

    @Autowired
    private AccountPayableRepository accountPayableRepository;

    @Autowired
    private BankIntegrationService bankIntegrationService;

    @Autowired
    private NotificationService notificationService;

    public void processPayment(Long accountPayableId) {
        log.info("Processing payment for account payable: {}", accountPayableId);

        AccountPayable accountPayable = accountPayableRepository.findById(accountPayableId)
                .orElseThrow(() -> new ResourceNotFoundException("Account Payable not found"));

        // Validar status
        if (accountPayable.getStatus() != PaymentStatus.APPROVED) {
            throw new BusinessException("Payment can only be processed for approved accounts");
        }

        try {
            // Processar cada parcela de pagamento
            for (PaymentSchedule schedule : accountPayable.getSchedules()) {
                if (schedule.getStatus() == PaymentStatus.SCHEDULED) {
                    processSchedule(schedule);
                }
            }

            // Atualizar status da conta
            updateAccountPayableStatus(accountPayable);

        } catch (Exception e) {
            log.error("Error processing payment: {}", e.getMessage());
            handlePaymentError(accountPayable, e);
        }
    }

    private void processSchedule(PaymentSchedule schedule) {
        // Criar requisição de pagamento
        PaymentRequest request = createPaymentRequest(schedule);

        // Enviar para integração bancária
        PaymentResponse response = bankIntegrationService.submitPayment(request);

        // Atualizar status baseado na resposta
        updateScheduleStatus(schedule, response);

        // Notificar resultado
        notifyPaymentResult(schedule, response);
    }

    private PaymentRequest createPaymentRequest(PaymentSchedule schedule) {
        AccountPayable accountPayable = schedule.getAccountPayable();

        return PaymentRequest.builder()
                .amount(schedule.getAmount())
                .scheduledDate(schedule.getScheduledDate())
                .paymentType(schedule.getPaymentType())
                .bankAccount(schedule.getBankAccount())
                .supplierInfo(createSupplierInfo(accountPayable.getSupplier()))
                .documentNumber(accountPayable.getDocumentNumber())
                .description(accountPayable.getDescription())
                .build();
    }

    private void updateScheduleStatus(PaymentSchedule schedule, PaymentResponse response) {
        schedule.setStatus(response.isSuccess() ? PaymentStatus.PAID : PaymentStatus.PENDING);
        // Adicionar informações de rastreamento do banco
        schedule.setBankTransactionId(response.getTransactionId());
    }

    private void updateAccountPayableStatus(AccountPayable accountPayable) {
        boolean allPaid = accountPayable.getSchedules().stream()
                .allMatch(schedule -> schedule.getStatus() == PaymentStatus.PAID);

        if (allPaid) {
            accountPayable.setStatus(PaymentStatus.PAID);
        }
    }

    private void handlePaymentError(AccountPayable accountPayable, Exception e) {
        notificationService.notifyPaymentError(accountPayable, e.getMessage());
        throw new PaymentProcessingException("Failed to process payment: " + e.getMessage(), e);
    }
}
