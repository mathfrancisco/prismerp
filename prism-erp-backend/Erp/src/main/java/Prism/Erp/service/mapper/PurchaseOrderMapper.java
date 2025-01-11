@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PurchaseOrderMapper {
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "supplierId", source = "supplier.id")
    @Mapping(target = "supplierName", source = "supplier.name")
    PurchaseOrderDTO toDTO(PurchaseOrder purchaseOrder);

    @Mapping(target = "supplier.id", source = "supplierId")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    PurchaseOrder toEntity(PurchaseOrderDTO purchaseOrderDTO);

    List<PurchaseOrderDTO> toDTOList(List<PurchaseOrder> purchaseOrders);

    @Mapping(target = "purchaseOrder", ignore = true)
    PurchaseOrderItemDTO toItemDTO(PurchaseOrderItem item);

    @Mapping(target = "purchaseOrder", ignore = true)
    PurchaseOrderItem toItemEntity(PurchaseOrderItemDTO itemDTO);

    @AfterMapping
    default void linkItems(@MappingTarget PurchaseOrder purchaseOrder) {
        if (purchaseOrder.getItems() != null) {
            purchaseOrder.getItems().forEach(item -> item.setPurchaseOrder(purchaseOrder));
        }
    }
}