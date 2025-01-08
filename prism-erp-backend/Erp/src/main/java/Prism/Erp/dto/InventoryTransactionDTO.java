package Prism.Erp.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InventoryTransactionDTO {
    private Long id;
    private Long productId;
    private String productName;
    private String type;
    private Integer quantity;
    private String reference;
    private String notes;
}