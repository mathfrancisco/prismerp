package Prism.Erp.repository.business.supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findByDocumentNumber(String documentNumber);
    
    List<Supplier> findByStatus(SupplierStatus status);
    
    @Query("SELECT s FROM Supplier s WHERE s.qualityRating >= :minRating AND s.status = 'ACTIVE'")
    List<Supplier> findQualifiedSuppliers(@Param("minRating") BigDecimal minRating);
    
    @Query("SELECT s FROM Supplier s JOIN s.categories c WHERE c IN :categories")
    List<Supplier> findByCategoriesIn(@Param("categories") Set<String> categories);
    
    @Query("SELECT s FROM Supplier s WHERE s.lastEvaluationDate <= :date")
    List<Supplier> findSuppliersNeedingEvaluation(@Param("date") LocalDate date);
}