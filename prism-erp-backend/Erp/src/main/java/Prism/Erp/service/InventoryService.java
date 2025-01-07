package Prism.Erp.service;

import Prism.Erp.dto.InventoryTransactionDTO;
import Prism.Erp.dto.InvoiceDTO;
import Prism.Erp.dto.InvoiceTaxCalculationDTO;
import Prism.Erp.dto.ProductStockDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;


public interface InventoryService {
    InventoryTransactionDTO createTransaction(InventoryTransactionDTO transactionDTO);
    Page<ProductStockDTO> getStockLevels(Pageable pageable); // Defina ProductStockDTO
    List<ProductStockDTO> getLowStockProducts();

}
