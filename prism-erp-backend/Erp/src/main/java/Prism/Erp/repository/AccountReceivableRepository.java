package Prism.Erp.repository;

import Prism.Erp.entity.AccountReceivable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AccountReceivableRepository extends JpaRepository<AccountReceivable, Long>,
        JpaSpecificationExecutor<AccountReceivable> {

    boolean existsByInvoiceId(Long invoiceId);
    List<AccountReceivable> findByCustomerId(Long customerId);
}