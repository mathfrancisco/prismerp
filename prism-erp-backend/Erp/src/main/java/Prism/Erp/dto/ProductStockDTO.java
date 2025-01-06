package Prism.Erp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockDTO {
    private Long productId;
    private String productName;
    private String productCode;
    private Integer currentStock;
    private Integer minimumStock;
    private String category;
    private Boolean lowStock;
}
