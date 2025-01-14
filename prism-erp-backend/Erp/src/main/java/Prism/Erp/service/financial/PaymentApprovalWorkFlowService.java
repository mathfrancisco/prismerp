package Prism.Erp.service.financial;


import Prism.Erp.dto.financial.PaymentApprovalDTO;
import Prism.Erp.entity.User;
import Prism.Erp.entity.financial.AccountPayable;
import Prism.Erp.entity.financial.ApprovalWorkflowConfig;
import Prism.Erp.entity.financial.PaymentApproval;
import Prism.Erp.exception.BusinessException;
import Prism.Erp.exception.ResourceNotFoundException;
import Prism.Erp.model.financial.ApprovalStatus;
import Prism.Erp.model.financial.PaymentStatus;
import Prism.Erp.repository.financial.PaymentApprovalRepository;
import Prism.Erp.service.NotificationService;
import Prism.Erp.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class PaymentApprovalWorkFlowService {
    @Autowired
    private ApprovalWorkflowConfigRepository workflowConfigRepository;

    @Autowired
    private PaymentApprovalRepository approvalRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    public void startApprovalWorkflow(AccountPayable accountPayable) {
        log.info("Starting approval workflow for account payable: {}", accountPayable.getDocumentNumber());

        // Buscar configuração do workflow baseado no valor
        ApprovalWorkflowConfig config = findApplicableWorkflowConfig(accountPayable.getAmount());

        // Criar aprovações necessárias
        List<PaymentApproval> approvals = createApprovalChain(accountPayable, config);

        // Notificar primeiro aprovador se for sequencial, ou todos se for paralelo
        if (config.getSequentialApproval()) {
            notificationService.notifyApprover(approvals.get(0));
        } else {
            approvals.forEach(notificationService::notifyApprover);
        }
    }

    public void processApproval(PaymentApprovalDTO approvalDTO) {
        PaymentApproval approval = approvalRepository.findById(approvalDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Approval not found"));

        // Validar se o usuário pode aprovar
        validateApprover(approval, approvalDTO.getApproverId());

        // Atualizar aprovação
        approval.setStatus(approvalDTO.getStatus());
        approval.setComments(approvalDTO.getComments());
        approval.setApprovalDate(LocalDateTime.now());

        approvalRepository.save(approval);

        // Processar próximos passos
        processNextStep(approval);
    }

    private void processNextStep(PaymentApproval approval) {
        AccountPayable accountPayable = approval.getAccountPayable();
        ApprovalWorkflowConfig config = findApplicableWorkflowConfig(accountPayable.getAmount());

        if (approval.getStatus() == ApprovalStatus.REJECTED) {
            rejectPayment(accountPayable, approval.getComments());
            return;
        }

        if (config.getSequentialApproval()) {
            processSequentialApproval(approval, config);
        } else {
            processParallelApproval(approval, config);
        }
    }

    private void processSequentialApproval(PaymentApproval approval, ApprovalWorkflowConfig config) {
        AccountPayable accountPayable = approval.getAccountPayable();
        List<PaymentApproval> approvals = approvalRepository.findByAccountPayableOrderByApprovalLevel(accountPayable);

        // Encontrar próxima aprovação pendente
        Optional<PaymentApproval> nextApproval = approvals.stream()
                .filter(a -> a.getStatus() == ApprovalStatus.PENDING)
                .findFirst();

        if (nextApproval.isPresent()) {
            notificationService.notifyApprover(nextApproval.get());
        } else if (isFullyApproved(approvals, config)) {
            approvePayment(accountPayable);
        }
    }

    private void processParallelApproval(PaymentApproval approval, ApprovalWorkflowConfig config) {
        AccountPayable accountPayable = approval.getAccountPayable();
        List<PaymentApproval> approvals = approvalRepository.findByAccountPayable(accountPayable);

        if (isFullyApproved(approvals, config)) {
            approvePayment(accountPayable);
        }
    }

    private boolean isFullyApproved(List<PaymentApproval> approvals, ApprovalWorkflowConfig config) {
        long approvedCount = approvals.stream()
                .filter(a -> a.getStatus() == ApprovalStatus.APPROVED)
                .count();

        return approvedCount >= config.getRequiredApprovals();
    }

    private void approvePayment(AccountPayable accountPayable) {
        accountPayable.setStatus(PaymentStatus.APPROVED);
        notificationService.notifyPaymentApproved(accountPayable);

        // Integração com processamento de pagamento
        // paymentProcessingService.schedulePayment(accountPayable);
    }

    private void rejectPayment(AccountPayable accountPayable, String reason) {
        accountPayable.setStatus(PaymentStatus.REJECTED);
        notificationService.notifyPaymentRejected(accountPayable, reason);
    }

    private List<PaymentApproval> createApprovalChain(AccountPayable accountPayable, ApprovalWorkflowConfig config) {
        List<PaymentApproval> approvals = new ArrayList<>();

        // Buscar aprovadores baseado nas roles configuradas
        List<User> approvers = userService.findUsersByRoles(config.getApproverRoles());

        for (int i = 0; i < config.getRequiredApprovals(); i++) {
            PaymentApproval approval = PaymentApproval.builder()
                    .accountPayable(accountPayable)
                    .approvalLevel(i + 1)
                    .status(ApprovalStatus.PENDING)
                    .build();

            approvals.add(approvalRepository.save(approval));
        }

        return approvals;
    }

    private ApprovalWorkflowConfig findApplicableWorkflowConfig(BigDecimal amount) {
        return workflowConfigRepository.findByAmountRange(amount)
                .orElseThrow(() -> new BusinessException("No workflow configuration found for amount: " + amount));
    }

    private void validateApprover(PaymentApproval approval, Long approverId) {
        User approver = userService.findById(approverId)
                .orElseThrow(() -> new ResourceNotFoundException("Approver not found"));

        ApprovalWorkflowConfig config = findApplicableWorkflowConfig(approval.getAccountPayable().getAmount());

        if (!hasApprovalRole(approver, config.getApproverRoles())) {
            throw new BusinessException("User does not have permission to approve this payment");
        }
    }

    private boolean hasApprovalRole(User user, List<String> requiredRoles) {
        return user.getRoles().stream()
                .map(Role::getName)
                .anyMatch(requiredRoles::contains);
    }
}
}
