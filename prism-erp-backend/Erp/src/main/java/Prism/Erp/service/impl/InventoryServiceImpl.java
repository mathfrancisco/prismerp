package Prism.Erp.service.impl;

import Prism.Erp.dto.InventoryTransactionDTO;
import Prism.Erp.dto.ProductStockDTO;
import Prism.Erp.entity.InventoryTransaction;
import Prism.Erp.entity.Product;
import Prism.Erp.entity.ProductStock;
import Prism.Erp.exception.ResourceNotFoundException;
import Prism.Erp.repository.InventoryTransactionRepository;
import Prism.Erp.repository.ProductRepository;
import Prism.Erp.repository.ProductStockRepository;
import Prism.Erp.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryTransactionRepository transactionRepository;
    private final ProductStockRepository stockRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public InventoryTransactionDTO createTransaction(InventoryTransactionDTO transactionDTO) {
        Product product = productRepository.findById(transactionDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        ProductStock stock = stockRepository.findByProductId(transactionDTO.getProductId())
                .orElseGet(() -> createInitialStock(product));

        // Update stock levels
        if ("OUTBOUND".equals(transactionDTO.getType())) {
            stock.setCurrentStock(stock.getCurrentStock() - transactionDTO.getQuantity());
        } else {
            stock.setCurrentStock(stock.getCurrentStock() + transactionDTO.getQuantity());
        }

        // Create transaction record
        InventoryTransaction transaction = InventoryTransaction.builder()
                .product(product)
                .type(transactionDTO.getType())
                .quantity(transactionDTO.getQuantity())
                .reference(transactionDTO.getReference())
                .notes(transactionDTO.getNotes())
                .transactionDate(LocalDateTime.now())
                .createdBy("SYSTEM") // Should come from security context
                .build();

        stockRepository.save(stock);
        transaction = transactionRepository.save(transaction);

        return convertTransactionToDTO(transaction);
    }

    @Override
    public Page<ProductStockDTO> getStockLevels(Pageable pageable) {
        return stockRepository.findAll(pageable)
                .map(this::convertStockToDTO);
    }

    @Override
    public List<ProductStockDTO> getLowStockProducts() {
        return stockRepository.findLowStockProducts().stream()
                .map(this::convertStockToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryTransactionDTO> getTransactionsByProductId(Long productId) {
        return transactionRepository.findByProductIdOrderByTransactionDateDesc(productId).stream()
                .map(this::convertTransactionToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductStockDTO getProductStock(Long productId) {
        return stockRepository.findByProductId(productId)
                .map(this::convertStockToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Product stock not found"));
    }

    private ProductStock createInitialStock(Product product) {
        return ProductStock.builder()
                .product(product)
                .currentStock(0)
                .minimumStock(0)
                .maximumStock(1000)
                .locationCode("DEFAULT")
                .status("ACTIVE")
                .build();
    }

    private ProductStockDTO convertStockToDTO(ProductStock stock) {
        return ProductStockDTO.builder()
                .productId(stock.getProduct().getId())
                .productName(stock.getProduct().getName())
                .currentStock(stock.getCurrentStock())
                .minimumStock(stock.getMinimumStock())
                .maximumStock(stock.getMaximumStock())
                .locationCode(stock.getLocationCode())
                .status(stock.getStatus())
                .build();
    }

    private InventoryTransactionDTO convertTransactionToDTO(InventoryTransaction transaction) {
        return InventoryTransactionDTO.builder()
                .id(transaction.getId())
                .productId(transaction.getProduct().getId())
                .type(transaction.getType())
                .quantity(transaction.getQuantity())
                .reference(transaction.getReference())
                .notes(transaction.getNotes())
                .transactionDate(transaction.getTransactionDate())
                .createdBy(transaction.getCreatedBy())
                .build();
    }
}