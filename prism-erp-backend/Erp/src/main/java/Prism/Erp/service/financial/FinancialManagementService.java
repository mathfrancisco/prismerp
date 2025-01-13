package Prism.Erp.service.financial;

// Serviço de Gestão Financeira
public interface FinancialManagementService {
    CashFlowDTO getDailyCashFlow();
    CashFlowDTO getProjectedCashFlow(LocalDate startDate, LocalDate endDate);
    FinancialMetricsDTO getFinancialMetrics(LocalDate referenceDate);
    BankReconciliationDTO reconcileBankStatement(Long bankAccountId, List<BankTransactionDTO> transactions);
}
