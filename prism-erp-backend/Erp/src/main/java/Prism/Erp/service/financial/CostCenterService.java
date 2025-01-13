package Prism.Erp.service.financial;

public interface CostCenterService {
    CostCenterDTO createCostCenter(CostCenterDTO costCenterDTO);
    void allocateExpense(Long costCenterId, ExpenseAllocationDTO allocation);
    CostCenterReportDTO getCostCenterReport(Long costCenterId, LocalDate startDate, LocalDate endDate);
    List<CostDistributionDTO> getDistributionByDepartment(LocalDate referenceDate);
}
