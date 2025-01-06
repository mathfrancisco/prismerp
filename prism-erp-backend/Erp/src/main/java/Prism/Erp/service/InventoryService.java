package Prism.Erp.service;

import Prism.Erp.dto.InventoryTransactionDTO;
import Prism.Erp.dto.InvoiceDTO;
import Prism.Erp.dto.InvoiceTaxCalculationDTO;
import Prism.Erp.dto.ProductStockDTO;
import Prism.Erp.model.InvoiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;


public interface InventoryService {
    InventoryTransactionDTO createTransaction(InventoryTransactionDTO transactionDTO);
    Page<ProductStockDTO> getStockLevels(Pageable pageable); // Defina ProductStockDTO
    List<ProductStockDTO> getLowStockProducts();
    InvoiceDTO generateInvoice(Long orderId);
    InvoiceDTO getInvoiceById(Long id);
    InvoiceDTO updateStatus(Long id, InvoiceStatus status);
    Page<InvoiceDTO> getAllInvoices(Pageable pageable);
    Page<InvoiceDTO> findInvoicesByStatus(InvoiceStatus status, Pageable pageable);
    InvoiceDTO applyDiscount(Long id, BigDecimal discountPercentage);
    InvoiceTaxCalculationDTO calculateTaxes(Long id);// Defina ProductStockDTO
}
