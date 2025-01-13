package Prism.Erp.dto.business.purchase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderHistoryDTO {
    private Long id;
    private Long purchaseOrderId;
    private List<PurchaseOrderEventDTO> events;
}