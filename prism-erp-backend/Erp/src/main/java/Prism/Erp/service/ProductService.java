package Prism.Erp.service;


import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import Prism.Erp.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    @Cacheable("products")
    ProductDTO getById(Long id);

    @Cacheable("products")
    Page<ProductDTO> findAll(Pageable pageable);

    @CacheEvict(value = "products", allEntries = true)
    ProductDTO create(ProductDTO productDTO);

    @CacheEvict(value = "products", allEntries = true)
    ProductDTO update(Long id, ProductDTO productDTO);

    @CacheEvict(value = "products", allEntries = true)
    void delete(Long id);

    List<ProductDTO> findByCategory(String category);
}