@Repository
public interface PurchaseOrderAttachmentRepository extends JpaRepository<PurchaseOrderAttachment, Long> {
    List<PurchaseOrderAttachment> findByPurchaseOrderId(Long purchaseOrderId);
    
    @Query("SELECT poa FROM PurchaseOrderAttachment poa WHERE poa.purchaseOrder.id = :purchaseOrderId AND poa.fileType IN :fileTypes")
    List<PurchaseOrderAttachment> findByPurchaseOrderIdAndFileTypes(
        @Param("purchaseOrderId") Long purchaseOrderId, 
        @Param("fileTypes") List<String> fileTypes
    );
    
    void deleteByPurchaseOrderIdAndFileName(Long purchaseOrderId, String fileName);
}
