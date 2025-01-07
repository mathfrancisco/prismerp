package Prism.Erp.service.impl;

import Prism.Erp.dto.InventoryTransactionDTO;
import Prism.Erp.dto.ProductStockDTO;
import Prism.Erp.entity.InventoryTransaction;
import Prism.Erp.entity.Product;
import Prism.Erp.model.TransactionType;
import Prism.Erp.repository.InventoryTransactionRepository;
import Prism.Erp.repository.ProductRepository;
import Prism.Erp.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryTransactionRepository inventoryTransactionRepository;
    private final ProductRepository productRepository;
    private static final int LOW_STOCK_THRESHOLD = 10; // Configure according to your needs

    @Override
    @Transactional
    public InventoryTransactionDTO createTransaction(InventoryTransactionDTO transactionDTO) {
        Product product = productRepository.findById(transactionDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        InventoryTransaction transaction = convertToEntity(transactionDTO);

        // Update product stock
        updateProductStock(product, transaction);
        productRepository.save(product);

        return convertToDTO(inventoryTransactionRepository.save(transaction));
    }

    @Override
    public Page<ProductStockDTO> getStockLevels(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(product -> ProductStockDTO.builder()
                        .productId(product.getId())
                        .productName(product.getName())
                        .productCode(product.getCode())
                        .currentStock(product.getCurrentStock())
                        .minimumStock(product.getMinimumStock())
                        .category(product.getCategory())
                        .lowStock(product.getCurrentStock() <= product.getMinimumStock())
                        .build());
    }

    @Override
    public List<ProductStockDTO> getLowStockProducts() {
        return productRepository.findByCurrentStockLessThanEqual(LOW_STOCK_THRESHOLD)
                .stream()
                .map(product -> ProductStockDTO.builder()
                        .productId(product.getId())
                        .productName(product.getName())
                        .productCode(product.getCode())
                        .currentStock(product.getCurrentStock())
                        .minimumStock(product.getMinimumStock())
                        .category(product.getCategory())
                        .lowStock(true)
                        .build())
                .collect(Collectors.toList());
    }

    private void updateProductStock(Product product, InventoryTransaction transaction) {
        if (transaction.getType() == TransactionType.INBOUND) {
            product.setCurrentStock(product.getCurrentStock() + transaction.getQuantity());
        } else if (transaction.getType() == TransactionType.OUTBOUND) {
            int newStock = product.getCurrentStock() - transaction.getQuantity();
            if (newStock < 0) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
            product.setCurrentStock(newStock);
        }
    }

    private InventoryTransactionDTO convertToDTO(InventoryTransaction entity) {
        return InventoryTransactionDTO.builder()
                .id(entity.getId())
                .productId(entity.getProduct().getId())
                .productName(entity.getProduct().getName())
                .type(entity.getType().name())
                .quantity(entity.getQuantity())
                .reference(entity.getReference())
                .notes(entity.getNotes())
                .build();
    }

    private InventoryTransaction convertToEntity(InventoryTransactionDTO dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return InventoryTransaction.builder()
                .product(product)
                .type(TransactionType.valueOf(dto.getType()))
                .quantity(dto.getQuantity())
                .reference(dto.getReference())
                .notes(dto.getNotes())
                .build();
    }

    @Override
    public List<InventoryTransactionDTO> getTransactionsByProductId(Long productId) {
        return inventoryTransactionRepository.findByProductId(productId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}