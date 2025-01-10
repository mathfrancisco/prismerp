
package Prism.Erp.service;
import Prism.Erp.dto.InventoryTransactionDTO;
import Prism.Erp.dto.ProductStockDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface InventoryService {
    /**
     * Creates a new inventory transaction
     */
    InventoryTransactionDTO createTransaction(InventoryTransactionDTO transactionDTO);

    /**
     * Gets current stock levels with pagination
     */
    Page<ProductStockDTO> getStockLevels(Pageable pageable);

    /**
     * Gets list of products with stock below minimum level
     */
    List<ProductStockDTO> getLowStockProducts();

    /**
     * Gets all transactions for a specific product
     */
    List<InventoryTransactionDTO> getTransactionsByProductId(Long productId);

    /**
     * Gets current stock information for a specific product
     */
    ProductStockDTO getProductStock(Long productId);
}