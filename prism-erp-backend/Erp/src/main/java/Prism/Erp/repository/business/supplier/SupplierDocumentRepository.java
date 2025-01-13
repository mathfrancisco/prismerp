package Prism.Erp.repository.business.supplier;

import Prism.Erp.entity.business.supplier.SupplierDocument;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierDocumentRepository extends JpaRepository<SupplierDocument, Long> {
    List<SupplierDocument> findBySupplierId(Long supplierId);
    
    @Query("SELECT sd FROM SupplierDocument sd WHERE sd.supplier.id = :supplierId AND sd.documentType = :documentType")
    Optional<SupplierDocument> findBySupplierIdAndDocumentType(
        @Param("supplierId") Long supplierId,
        @Param("documentType") String documentType
    );
    
    @Query("SELECT sd FROM SupplierDocument sd WHERE sd.expirationDate <= :date AND sd.status = 'ACTIVE'")
    List<SupplierDocument> findExpiringDocuments(@Param("date") LocalDate date);
}