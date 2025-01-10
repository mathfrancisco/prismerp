package Prism.Erp.repository;

import Prism.Erp.entity.Sale;
import Prism.Erp.model.SaleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    Page<Sale> findByCustomerId(Long customerId, Pageable pageable);
    List<Sale> findByStatusAndSaleDateBetween(SaleStatus status, LocalDateTime startDate, LocalDateTime endDate);
    Page<Sale> findByStatus(SaleStatus status, Pageable pageable);
}

