package Prism.Erp.dto.business.purchase;

import Prism.Erp.model.business.FreightType;
import Prism.Erp.model.business.PurchaseOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderDTO {
    private Long id;
    private String orderNumber;
    private Long supplierId;
    private String supplierName;
    private LocalDateTime orderDate;
    private LocalDateTime expectedDeliveryDate;
    private PurchaseOrderStatus status;
    private FreightType freightType;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal freightCost;
    private BigDecimal totalAmount;
    private String notes;
    private List<PurchaseOrderItemDTO> items;
    private List<PurchaseOrderPaymentDTO> payments;
    private List<PurchaseOrderApprovalDTO> approvals;
    private PurchaseOrderHistoryDTO history;
    private List<PurchaseOrderAttachmentDTO> attachments;
}