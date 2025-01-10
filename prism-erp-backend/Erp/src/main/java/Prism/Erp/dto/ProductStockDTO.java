package Prism.Erp.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductStockDTO {
    private Long productId;
    private String productName;
    private String productCode;
    private Integer currentStock;
    private Integer minimumStock;
    private Integer maximumStock;

    private String category;
    private String locationCode;
    private Boolean lowStock;
    private String status;
}
