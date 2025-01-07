package Prism.Erp.repository;

import Prism.Erp.entity.SalesOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long>, JpaSpecificationExecutor<SalesOrder> {
    Optional<SalesOrder> findByOrderNumber(String orderNumber);
    List<SalesOrder> findByCustomerId(Long customerId);
    Page<SalesOrder> findByCustomerId(Long customerId, Pageable pageable); // Novo método com paginação
}
