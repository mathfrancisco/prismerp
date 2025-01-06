package Prism.Erp.service.impl;

import Prism.Erp.dto.InventoryTransactionDTO;
import Prism.Erp.entity.InventoryTransaction;
import Prism.Erp.repository.ProductRepository;
import Prism.Erp.service.AbstractCrudService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl extends AbstractCrudService<InventoryTransaction, InventoryTransactionDTO> {
    private final ProductRepository productRepository;

    @Override
    protected InventoryTransactionDTO toDTO(InventoryTransaction entity) {
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

    @Override
    protected String getEntityName() {
        return "InventoryTransaction";
    }
}
