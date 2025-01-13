public interface IncomeStatementService {
    IncomeStatementDTO generateIncomeStatement(LocalDate startDate, LocalDate endDate);
    List<RevenueBySourceDTO> getRevenueBySource(LocalDate referenceDate);
    List<ExpenseByTypeDTO> getExpensesByType(LocalDate referenceDate);
    ProfitabilityAnalysisDTO getProfitabilityAnalysis(LocalDate startDate, LocalDate endDate);
}
