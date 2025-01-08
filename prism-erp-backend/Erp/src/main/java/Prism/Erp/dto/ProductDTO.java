package Prism.Erp.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
