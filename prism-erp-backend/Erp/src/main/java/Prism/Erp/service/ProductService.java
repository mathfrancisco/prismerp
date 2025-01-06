package Prism.Erp.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import Prism.Erp.dto.ProductDTO;

public interface ProductService {
    ProductDTO create(ProductDTO productDTO);
    ProductDTO update(Long id, ProductDTO productDTO);
    ProductDTO getById(Long id);
    Page<ProductDTO> findAll(Pageable pageable);
    void delete(Long id);
}