package Prism.Erp.repository.business.supplier;

import Prism.Erp.entity.business.supplier.SupplierContact;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SupplierContactRepository extends JpaRepository<SupplierContact, Long> {
    List<SupplierContact> findBySupplierId(Long supplierId);
    
    @Query("SELECT sc FROM SupplierContact sc WHERE sc.supplier.id = :supplierId AND sc.contactDate >= :startDate")
    List<SupplierContact> findBySupplierIdAndDateAfter(
        @Param("supplierId") Long supplierId,
        @Param("startDate") LocalDateTime startDate
    );
    
    @Query("SELECT sc FROM SupplierContact sc WHERE sc.supplier.id = :supplierId ORDER BY sc.contactDate DESC")
    Page<SupplierContact> findLatestContacts(
        @Param("supplierId") Long supplierId, 
        Pageable pageable
    );
}
