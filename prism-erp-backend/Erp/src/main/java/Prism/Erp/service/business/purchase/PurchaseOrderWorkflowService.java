package Prism.Erp.service.business.purchase;

import Prism.Erp.dto.business.purchase.PurchaseOrderApprovalDTO;
import Prism.Erp.entity.business.purchase.PurchaseOrder;
import Prism.Erp.model.business.ApprovalStatus;
import Prism.Erp.model.business.PurchaseOrderStatus;
import Prism.Erp.repository.business.purchase.PurchaseOrderHistoryRepository;
import Prism.Erp.service.NotificationService;
import Prism.Erp.service.business.integration.FinancialIntegrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

// PurchaseOrderWorkflowService.java
@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseOrderWorkflowService {
    private final PurchaseOrderHistoryRepository historyRepository;
    private final NotificationService notificationService;
    private final FinancialIntegrationService financialService;

    public void initializeWorkflow(PurchaseOrder purchaseOrder) {
        createHistory(purchaseOrder, "Order Created", "Initial order creation");
        notificationService.notifyPurchaseOrderCreated(purchaseOrder);
    }

    public void handleOrderUpdate(PurchaseOrder purchaseOrder) {
        createHistory(purchaseOrder, "Order Updated", "Order details updated");
        notificationService.notifyPurchaseOrderUpdated(purchaseOrder);
    }

    public void handleStatusChange(PurchaseOrder purchaseOrder, PurchaseOrderStatus newStatus) {
        createHistory(purchaseOrder, "Status Changed", 
            String.format("Status changed from %s to %s", purchaseOrder.getStatus(), newStatus));
            
        switch (newStatus) {
            case APPROVED:
                handleApproval(purchaseOrder);
                break;
            case SENT_TO_SUPPLIER:
                notifySupplier(purchaseOrder);
                break;
            case COMPLETED:
                finalizeOrder(purchaseOrder);
                break;
        }
    }

    public void processApproval(PurchaseOrder purchaseOrder, PurchaseOrderApprovalDTO approvalDTO) {
        createHistory(purchaseOrder, "Approval Processed", 
            String.format("Approval processed by %s: %s", approvalDTO.getApproverName(), 
                         approvalDTO.getStatus()));
                         
        if (approvalDTO.getStatus() == ApprovalStatus.APPROVED) {
            purchaseOrder.setStatus(PurchaseOrderStatus.APPROVED);
            handleApproval(purchaseOrder);
        } else if (approvalDTO.getStatus() == ApprovalStatus.REJECTED) {
            purchaseOrder.setStatus(PurchaseOrderStatus.REJECTED);
            handleRejection(purchaseOrder, approvalDTO.getComments());
        }
    }

    private void createHistory(PurchaseOrder purchaseOrder, String event, String description) {
        PurchaseOrderHistory history = purchaseOrder.getHistory();
        if (history == null) {
            history = new PurchaseOrderHistory();
            history.setPurchaseOrder(purchaseOrder);
            purchaseOrder.setHistory(history);
        }
        
        PurchaseOrderEvent historyEvent = PurchaseOrderEvent.builder()
            .event(event)
            .description(description)
            .timestamp(LocalDateTime.now())
            .username(SecurityContextHolder.getContext().getAuthentication().getName())
            .build();
            
        history.getEvents().add(historyEvent);
    }
}