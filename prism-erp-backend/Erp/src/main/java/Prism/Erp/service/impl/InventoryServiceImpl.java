package Prism.Erp.service.impl;

import Prism.Erp.dto.InventoryTransactionDTO;
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

import java.util.List;


@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryTransactionRepository inventoryTransactionRepository;
    private final ProductRepository productRepository;

    @Override
    public InventoryTransactionDTO createTransaction(InventoryTransactionDTO transactionDTO) {
        InventoryTransaction transaction = convertToEntity(transactionDTO);
        return convertToDTO(inventoryTransactionRepository.save(transaction));
    }

    // Implemente os métodos getStockLevels e getLowStockProducts
    // ... (Implementação dos métodos getStockLevels e getLowStockProducts usando consultas personalizadas ou outras lógicas)

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
                .orElseThrow(() -> new RuntimeException("Produto não encontrado")); // Exceção mais específica

        return InventoryTransaction.builder()
                .product(product)
                .type(TransactionType.valueOf(dto.getType())) // Certifique-se de que o tipo seja válido
                .quantity(dto.getQuantity())
                .reference(dto.getReference())
                .notes(dto.getNotes())
                .build();
    }
}