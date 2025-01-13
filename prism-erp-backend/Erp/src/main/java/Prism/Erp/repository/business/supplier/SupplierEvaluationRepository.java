package Prism.Erp.repository.business.supplier;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface SupplierEvaluationRepository extends JpaRepository<SupplierEvaluation, Long> {
    Optional<SupplierEvaluation> findTopBySupplierIdOrderByEvaluationDateDesc(Long supplierId);
    
    @Query("SELECT se FROM SupplierEvaluation se WHERE se.supplier.id = :supplierId AND se.evaluationDate BETWEEN :startDate AND :endDate")
    List<SupplierEvaluation> findBySupplierIdAndDateRange(
        @Param("supplierId") Long supplierId,
        @Param("startDate") LocalDate startDate, 
        @Param("endDate") LocalDate endDate
    );
    
    @Query("SELECT AVG(se.overallScore) FROM SupplierEvaluation se WHERE se.supplier.id = :supplierId")
    Double getAverageEvaluationScore(@Param("supplierId") Long supplierId);
}