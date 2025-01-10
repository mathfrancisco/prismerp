package Prism.Erp.repository;

import Prism.Erp.entity.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {
    Optional<ProductStock> findByProductId(Long productId);

    @Query("SELECT ps FROM ProductStock ps WHERE ps.currentStock <= ps.minimumStock")
    List<ProductStock> findLowStockProducts();
}