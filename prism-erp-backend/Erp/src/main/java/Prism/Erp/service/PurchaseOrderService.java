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
