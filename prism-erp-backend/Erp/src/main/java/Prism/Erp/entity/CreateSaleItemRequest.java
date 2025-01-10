package Prism.Erp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSaleItemRequest {
    private Long productId;
    private Integer quantity;
    private BigDecimal discount;
    private BigDecimal unitPrice;

}
