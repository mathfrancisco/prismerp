package Prism.Erp.service.impl;

import Prism.Erp.dto.ProductDTO;
import Prism.Erp.entity.Product;
import Prism.Erp.exception.BusinessException;
import Prism.Erp.exception.ResourceNotFoundException;
import Prism.Erp.repository.ProductRepository;
import Prism.Erp.service.ProductService;
import jakarta.persistence.Cacheable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductDTO create(ProductDTO productDTO) {
        if (productRepository.findByCode(productDTO.getCode()).isPresent()) {
            throw new BusinessException("Já existe um produto com este código.");
        }

        Product product = convertToEntity(productDTO);
        return convertToDTO(productRepository.save(product)); // Retorna o DTO diretamente
    }

    @Override
    public ProductDTO getById(Long id) {
        log.info("Buscando produto com ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        return convertToDTO(product);
    }

    @Override
    public ProductDTO update(Long id, ProductDTO productDTO) {
        log.info("Atualizando produto com ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        updateProductFromDTO(product, productDTO);
        return convertToDTO(productRepository.save(product));
    }


    @Override
    public Page<ProductDTO> findAll(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }


    private Product convertToEntity(ProductDTO productDTO) {
        return Product.builder()
                .code(productDTO.getCode())
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .currentStock(productDTO.getCurrentStock())
                .minimumStock(productDTO.getMinimumStock())
                .category(productDTO.getCategory())
                .unit(productDTO.getUnit())
                .active(productDTO.getActive())
                .build();
    }

    private ProductDTO convertToDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .currentStock(product.getCurrentStock())
                .minimumStock(product.getMinimumStock())
                .category(product.getCategory())
                .unit(product.getUnit())
                .active(product.getActive())
                .build();
    }

    private void updateProductFromDTO(Product product, ProductDTO productDTO) {
        product.setCode(productDTO.getCode());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setCurrentStock(productDTO.getCurrentStock());
        product.setMinimumStock(productDTO.getMinimumStock());
        product.setCategory(productDTO.getCategory());
        product.setUnit(productDTO.getUnit());
        product.setActive(productDTO.getActive());
    }
}
