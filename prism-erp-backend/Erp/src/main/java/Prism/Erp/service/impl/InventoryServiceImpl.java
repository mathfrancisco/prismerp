package Prism.Erp.service.impl;

import Prism.Erp.dto.InventoryTransactionDTO;
import Prism.Erp.dto.ProductStockDTO;
import Prism.Erp.entity.InventoryTransaction;
import Prism.Erp.entity.Product;
import Prism.Erp.entity.ProductStock;
import Prism.Erp.exception.InsufficientStockException;
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

        int previousQuantity = stock.getCurrentStock();
        int newQuantity;

        // Update stock levels based on transaction type
        if (transactionDTO.getTransactionType().isDecrease()) {
            newQuantity = previousQuantity - transactionDTO.getQuantity();
            if (newQuantity < 0) {
                throw new InsufficientStockException("Insufficient stock for product: " + product.getId());
            }
        } else {
            newQuantity = previousQuantity + transactionDTO.getQuantity();
        }
        stock.setCurrentStock(newQuantity);

        // Create transaction record
        InventoryTransaction transaction = InventoryTransaction.builder()
                .product(product)
                .transactionType(transactionDTO.getTransactionType())
                .quantity(transactionDTO.getQuantity())
                .previousQuantity(previousQuantity)
                .newQuantity(newQuantity)
                .referenceId(transactionDTO.getReferenceId())
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

    // Add this method to convert entity to DTO
    private InventoryTransactionDTO convertTransactionToDTO(InventoryTransaction transaction) {
        return InventoryTransactionDTO.builder()
                .id(transaction.getId())
                .productId(transaction.getProduct().getId())
                .transactionType(transaction.getTransactionType())
                .quantity(transaction.getQuantity())
                .previousQuantity(transaction.getPreviousQuantity())
                .newQuantity(transaction.getNewQuantity())
                .referenceId(transaction.getReferenceId())
                .notes(transaction.getNotes())
                .transactionDate(transaction.getTransactionDate())
                .createdBy(transaction.getCreatedBy())
                .build();
    }
}