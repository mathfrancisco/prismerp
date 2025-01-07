package Prism.Erp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String code;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer currentStock;
    private Integer minimumStock;
    private String category;
    private String unit;
    private Boolean active;
}
