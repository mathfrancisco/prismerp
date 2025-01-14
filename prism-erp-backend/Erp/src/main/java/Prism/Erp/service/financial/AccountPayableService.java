package Prism.Erp.service.financial;

import Prism.Erp.dto.financial.AccountPayableDTO;
import Prism.Erp.dto.financial.PaymentScheduleDTO;

import java.time.LocalDate;
import java.util.List;

public interface AccountPayableService {
    AccountPayableDTO createPayable(AccountPayableDTO payableDTO);
    void processPayment(Long payableId, PaymentDTO payment);
    void approvePayable(Long payableId, ApprovalDTO approval);
    List<AccountPayableDTO> getPendingPayables();
    PaymentScheduleDTO generatePaymentSchedule(LocalDate startDate, LocalDate endDate);
}
