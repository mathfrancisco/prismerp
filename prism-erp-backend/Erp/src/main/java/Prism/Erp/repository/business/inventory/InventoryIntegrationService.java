package Prism.Erp.repository.business.inventory;

import Prism.Erp.dto.business.purchase.PurchaseOrderDTO;
import Prism.Erp.dto.business.purchase.PurchaseOrderItemDTO;
import Prism.Erp.entity.business.inventory.InventoryItem;
import Prism.Erp.entity.business.inventory.InventoryMovement;
import Prism.Erp.model.NotificationPriority;
import Prism.Erp.model.business.AuditAction;
import Prism.Erp.model.business.MovementType;
import Prism.Erp.service.NotificationService;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class InventoryIntegrationService {

    private final InventoryItemRepository itemRepository;
    private final InventoryMovementRepository movementRepository;
    private final NotificationService notificationService;

    @Auditable(entity = "InventoryItem", action = AuditAction.UPDATE)
    public void processPurchaseOrderReceipt(PurchaseOrderDTO purchaseOrder) {
        for (PurchaseOrderItemDTO item : purchaseOrder.getItems()) {
            processItemReceipt(item, purchaseOrder);
        }
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    private void processItemReceipt(PurchaseOrderItemDTO item, PurchaseOrderDTO purchaseOrder) {
        InventoryItem inventoryItem = itemRepository.findBySku(item.getProductCode())
                .orElseThrow(() -> new ItemNotFoundException(item.getProductCode()));

        // Atualiza o estoque
        inventoryItem.setQuantity(inventoryItem.getQuantity().add(item.getQuantity()));

        // Registra o movimento
        InventoryMovement movement = new InventoryMovement();
        movement.setItem(inventoryItem);
        movement.setType(MovementType.PURCHASE_RECEIPT);
        movement.setQuantity(item.getQuantity());
        movement.setReferenceType("PURCHASE_ORDER");
        movement.setReferenceId(purchaseOrder.getOrderNumber());
        movement.setRequestedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        movement.setMovementDate(LocalDateTime.now());

        movementRepository.save(movement);

        // Verifica níveis de estoque
        checkStockLevels(inventoryItem);
    }

    private void checkStockLevels(InventoryItem item) {
        if (item.getQuantity().compareTo(item.getMinimumStock()) <= 0) {
            notificationService.createStockAlert(
                    item.getSku(),
                    "Stock below minimum level",
                    NotificationPriority.HIGH
            );
        } else if (item.getQuantity().compareTo(item.getReorderPoint()) <= 0) {
            notificationService.createStockAlert(
                    item.getSku(),
                    "Stock at reorder point",
                    NotificationPriority.MEDIUM
            );
        } else if (item.getQuantity().compareTo(item.getMaximumStock()) >= 0) {
            notificationService.createStockAlert(
                    item.getSku(),
                    "Stock above maximum level",
                    NotificationPriority.LOW
            );
        }
    }
}
