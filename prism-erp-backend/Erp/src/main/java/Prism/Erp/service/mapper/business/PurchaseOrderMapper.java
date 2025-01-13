package Prism.Erp.service.mapper.business;

import Prism.Erp.dto.business.purchase.PurchaseOrderDTO;
import Prism.Erp.dto.business.purchase.PurchaseOrderItemDTO;
import Prism.Erp.entity.business.purchase.PurchaseOrder;
import Prism.Erp.entity.business.purchase.PurchaseOrderItem;
import org.springframework.web.bind.annotation.Mapping;

import java.util.List;

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