package Prism.Erp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryTransactionDTO {
    private Long id;
    private Long productId;
    private String productName;
    private String type;
    private Integer quantity;
    private String reference;
    private String notes;
}