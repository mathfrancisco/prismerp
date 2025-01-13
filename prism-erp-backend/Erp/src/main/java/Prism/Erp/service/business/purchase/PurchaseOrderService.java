package Prism.Erp.service.business.purchase;

import Prism.Erp.dto.business.purchase.PurchaseOrderApprovalDTO;
import Prism.Erp.dto.business.purchase.PurchaseOrderAttachmentDTO;
import Prism.Erp.dto.business.purchase.PurchaseOrderDTO;
import Prism.Erp.dto.business.purchase.PurchaseOrderPaymentDTO;
import Prism.Erp.model.business.PurchaseOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PurchaseOrderService {
    PurchaseOrderDTO createPurchaseOrder(PurchaseOrderDTO purchaseOrderDTO);
    PurchaseOrderDTO findById(Long id);
    Page<PurchaseOrderDTO> findAll(Pageable pageable);
    PurchaseOrderDTO updatePurchaseOrder(Long id, PurchaseOrderDTO purchaseOrderDTO);
    void deletePurchaseOrder(Long id);
    PurchaseOrderDTO updateStatus(Long id, PurchaseOrderStatus status);
    PurchaseOrderDTO approvePurchaseOrder(Long id, PurchaseOrderApprovalDTO approvalDTO);
    List<PurchaseOrderDTO> findBySupplier(Long supplierId);
    void addAttachment(Long id, PurchaseOrderAttachmentDTO attachmentDTO);
    void processPayment(Long id, PurchaseOrderPaymentDTO paymentDTO);
}
