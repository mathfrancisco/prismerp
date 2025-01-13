package Prism.Erp.service.financial;

public interface AccountPayableService {
    AccountPayableDTO createPayable(AccountPayableDTO payableDTO);
    void processPayment(Long payableId, PaymentDTO payment);
    void approvePayable(Long payableId, ApprovalDTO approval);
    List<AccountPayableDTO> getPendingPayables();
    PaymentScheduleDTO generatePaymentSchedule(LocalDate startDate, LocalDate endDate);
}
