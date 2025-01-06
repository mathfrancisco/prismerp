package Prism.Erp.repository;

import Prism.Erp.entity.Invoice;
import Prism.Erp.model.InvoiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long>, JpaSpecificationExecutor<Invoice> {
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
    Optional<Invoice> findBySalesOrderId(Long salesOrderId);
    Page<Invoice> findByStatus(InvoiceStatus status, Pageable pageable);
}