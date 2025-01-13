public interface InvestmentManagementService {
    InvestmentDTO createInvestment(InvestmentDTO investmentDTO);
    InvestmentPerformanceDTO getInvestmentPerformance(Long investmentId);
    PortfolioAnalysisDTO getPortfolioAnalysis();
    List<InvestmentOpportunityDTO> getInvestmentOpportunities();
}
