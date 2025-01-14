package Prism.Erp.service.financial.impl;

import Prism.Erp.dto.financial.AccountPayableDTO;
import Prism.Erp.dto.financial.PaymentScheduleDTO;
import Prism.Erp.entity.business.supplier.Supplier;
import Prism.Erp.entity.financial.AccountPayable;
import Prism.Erp.entity.financial.CostCenter;
import Prism.Erp.entity.financial.PaymentApproval;
import Prism.Erp.entity.financial.PaymentSchedule;
import Prism.Erp.exception.BusinessException;
import Prism.Erp.exception.ResourceNotFoundException;
import Prism.Erp.model.financial.ApprovalStatus;
import Prism.Erp.model.financial.PaymentStatus;
import Prism.Erp.repository.business.supplier.SupplierRepository;
import Prism.Erp.repository.financial.AccountPayableRepository;
import Prism.Erp.repository.financial.CostCenterRepository;
import Prism.Erp.service.NotificationService;
import Prism.Erp.service.financial.AccountPayableService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class AccountPayableServiceImpl implements AccountPayableService {

    @Autowired
    private AccountPayableRepository accountPayableRepository;

    @Autowired
    private PaymentApprovalRepository approvalRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private CostCenterRepository costCenterRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    public AccountPayableDTO create(AccountPayableDTO dto) {
        log.info("Creating new account payable: {}", dto.getDocumentNumber());

        // Validar dados
        validateAccountPayable(dto);

        // Buscar entidades relacionadas
        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        CostCenter costCenter = dto.getCostCenterId() != null ?
                costCenterRepository.findById(dto.getCostCenterId())
                        .orElseThrow(() -> new ResourceNotFoundException("Cost Center not found"))
                : null;

        // Criar conta a pagar
        AccountPayable accountPayable = AccountPayable.builder()
                .documentNumber(dto.getDocumentNumber())
                .amount(dto.getAmount())
                .dueDate(dto.getDueDate())
                .issueDate(dto.getIssueDate())
                .status(PaymentStatus.PENDING)
                .description(dto.getDescription())
                .supplier(supplier)
                .costCenter(costCenter)
                .build();

        accountPayable = accountPayableRepository.save(accountPayable);

        // Criar programação de pagamentos
        if (dto.getSchedules() != null) {
            List<PaymentSchedule> schedules = createPaymentSchedules(accountPayable, dto.getSchedules());
            accountPayable.setSchedules(schedules);
        }

        // Iniciar workflow de aprovação
        startApprovalWorkflow(accountPayable);

        return convertToDTO(accountPayable);
    }

    @Override
    public AccountPayableDTO update(Long id, AccountPayableDTO dto) {
        AccountPayable existingPayable = accountPayableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account Payable not found"));

        // Atualizar apenas campos permitidos
        existingPayable.setDescription(dto.getDescription());
        existingPayable.setDueDate(dto.getDueDate());

        // Verificar se pode atualizar status
        if (canUpdateStatus(existingPayable, dto.getStatus())) {
            existingPayable.setStatus(dto.getStatus());
        }

        return convertToDTO(accountPayableRepository.save(existingPayable));
    }

    @Override
    public void delete(Long id) {
        AccountPayable accountPayable = accountPayableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account Payable not found"));

        if (accountPayable.getStatus() != PaymentStatus.PENDING) {
            throw new BusinessException("Cannot delete account payable in current status");
        }

        accountPayableRepository.delete(accountPayable);
    }

    private void validateAccountPayable(AccountPayableDTO dto) {
        if (dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Amount must be greater than zero");
        }
        if (dto.getDueDate().isBefore(LocalDate.now())) {
            throw new BusinessException("Due date cannot be in the past");
        }
    }

    private List<PaymentSchedule> createPaymentSchedules(AccountPayable accountPayable, List<PaymentScheduleDTO> schedules) {
        return schedules.stream()
                .map(scheduleDTO -> PaymentSchedule.builder()
                        .accountPayable(accountPayable)
                        .amount(scheduleDTO.getAmount())
                        .scheduledDate(scheduleDTO.getScheduledDate())
                        .paymentType(scheduleDTO.getPaymentType())
                        .bankAccount(scheduleDTO.getBankAccount())
                        .status(PaymentStatus.SCHEDULED)
                        .build())
                .collect(Collectors.toList());
    }

    private void startApprovalWorkflow(AccountPayable accountPayable) {
        // Criar primeira aprovação
        PaymentApproval approval = PaymentApproval.builder()
                .accountPayable(accountPayable)
                .approvalLevel(1)
                .status(ApprovalStatus.PENDING)
                .build();

        approvalRepository.save(approval);

        // Notificar aprovadores
        notificationService.notifyApprovers(approval);
    }
}
