package Prism.Erp.service.impl;

import Prism.Erp.entity.Product;
import Prism.Erp.repository.ProductRepository;
import Prism.Erp.service.ProductService;
import jakarta.persistence.Cacheable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CacheManager cacheManager;

    @Override
    @Cacheable(value = "products", key = "#id")
    public ProductDTO getById(Long id) {
        log.info("Fetching product with id: {}", id);
        return productRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @Override
    @CacheEvict(value = "products", key = "#id")
    public ProductDTO update(Long id, ProductDTO productDTO) {
        log.info("Updating product with id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        updateProductFromDTO(product, productDTO);
        return convertToDTO(productRepository.save(product));
    }
}
