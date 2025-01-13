package Prism.Erp.service.business.purchase;

import Prism.Erp.dto.business.purchase.PurchaseOrderApprovalDTO;
import Prism.Erp.dto.business.purchase.PurchaseOrderDTO;
import Prism.Erp.entity.business.purchase.PurchaseOrder;
import Prism.Erp.exception.PurchaseOrderException;
import Prism.Erp.model.business.PurchaseOrderStatus;
import Prism.Erp.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderMapper purchaseOrderMapper;
    private final SupplierService supplierService;
    private final PurchaseOrderValidationService validationService;
    private final PurchaseOrderWorkflowService workflowService;
    private final NotificationService notificationService;

    @Override
    public PurchaseOrderDTO createPurchaseOrder(PurchaseOrderDTO purchaseOrderDTO) {
        log.info("Creating new purchase order for supplier: {}", purchaseOrderDTO.getSupplierId());
        
        validationService.validateNewPurchaseOrder(purchaseOrderDTO);
        
        PurchaseOrder purchaseOrder = purchaseOrderMapper.toEntity(purchaseOrderDTO);
        purchaseOrder.setStatus(PurchaseOrderStatus.DRAFT);
        purchaseOrder.setOrderNumber(generateOrderNumber());
        
        calculateTotals(purchaseOrder);
        
        PurchaseOrder savedOrder = purchaseOrderRepository.save(purchaseOrder);
        workflowService.initializeWorkflow(savedOrder);
        notificationService.notifyPurchaseOrderCreated(savedOrder);
        
        return purchaseOrderMapper.toDTO(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderDTO findById(Long id) {
        return purchaseOrderRepository.findById(id)
            .map(purchaseOrderMapper::toDTO)
            .orElseThrow(() -> new PurchaseOrderException.OrderNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseOrderDTO> findAll(Pageable pageable) {
        return purchaseOrderRepository.findAll(pageable)
            .map(purchaseOrderMapper::toDTO);
    }

    @Override
    public PurchaseOrderDTO updatePurchaseOrder(Long id, PurchaseOrderDTO purchaseOrderDTO) {
        log.info("Updating purchase order: {}", id);
        
        PurchaseOrder existingOrder = purchaseOrderRepository.findById(id)
            .orElseThrow(() -> new PurchaseOrderException.OrderNotFoundException(id));
            
        validationService.validateUpdatePurchaseOrder(existingOrder, purchaseOrderDTO);
        
        updateOrderDetails(existingOrder, purchaseOrderDTO);
        calculateTotals(existingOrder);
        
        PurchaseOrder updatedOrder = purchaseOrderRepository.save(existingOrder);
        workflowService.handleOrderUpdate(updatedOrder);
        
        return purchaseOrderMapper.toDTO(updatedOrder);
    }

    @Override
    public PurchaseOrderDTO updateStatus(Long id, PurchaseOrderStatus newStatus) {
        log.info("Updating status of purchase order: {} to {}", id, newStatus);
        
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
            .orElseThrow(() -> new PurchaseOrderException.OrderNotFoundException(id));
            
        validationService.validateStatusTransition(purchaseOrder, newStatus);
        
        purchaseOrder.setStatus(newStatus);
        workflowService.handleStatusChange(purchaseOrder, newStatus);
        
        return purchaseOrderMapper.toDTO(purchaseOrderRepository.save(purchaseOrder));
    }

    @Override
    public PurchaseOrderDTO approvePurchaseOrder(Long id, PurchaseOrderApprovalDTO approvalDTO) {
        log.info("Processing approval for purchase order: {}", id);
        
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
            .orElseThrow(() -> new PurchaseOrderException.OrderNotFoundException(id));
            
        validationService.validateApproval(purchaseOrder, approvalDTO);
        
        workflowService.processApproval(purchaseOrder, approvalDTO);
        
        return purchaseOrderMapper.toDTO(purchaseOrderRepository.save(purchaseOrder));
    }

    private void calculateTotals(PurchaseOrder purchaseOrder) {
        BigDecimal subtotal = purchaseOrder.getItems().stream()
            .map(item -> item.getUnitPrice().multiply(item.getQuantity()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        BigDecimal taxAmount = subtotal.multiply(new BigDecimal("0.15")); // Example tax calculation
        
        purchaseOrder.setSubtotal(subtotal);
        purchaseOrder.setTaxAmount(taxAmount);
        purchaseOrder.setTotalAmount(subtotal.add(taxAmount).add(purchaseOrder.getFreightCost()));
    }

    private String generateOrderNumber() {
        // Implementation for generating unique order number
        return "PO-" + LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE)
               + "-" + RandomStringUtils.randomNumeric(4);
    }
}
