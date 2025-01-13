package Prism.Erp.repository.financial;

import Prism.Erp.entity.financial.FinancialTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, Long>,
        JpaSpecificationExecutor<FinancialTransaction> {
}