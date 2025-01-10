package Prism.Erp.service;

import Prism.Erp.dto.SaleDTO;
import Prism.Erp.entity.CreateSaleRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SaleService {
    /**
     * Creates a new sale from the provided request
     * @param request Sale creation request containing customer and items details
     * @return Created sale data
     */
    SaleDTO createSale(CreateSaleRequest request);

    /**
     * Confirms a sale, processing inventory transactions
     * @param saleId ID of the sale to confirm
     * @return Updated sale data
     */
    SaleDTO confirmSale(Long saleId);

    /**
     * Cancels a sale, reversing inventory transactions if necessary
     * @param saleId ID of the sale to cancel
     * @return Updated sale data
     */
    SaleDTO cancelSale(Long saleId);

    /**
     * Retrieves a paginated list of all sales
     * @param pageable Pagination information
     * @return Page of sales
     */
    Page<SaleDTO> getSales(Pageable pageable);

    /**
     * Retrieves a specific sale by ID
     * @param id Sale ID
     * @return Sale data
     */
    SaleDTO getSale(Long id);
}