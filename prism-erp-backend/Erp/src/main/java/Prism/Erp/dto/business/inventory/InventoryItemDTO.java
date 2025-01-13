package Prism.Erp.dto.business.inventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemDTO {
    private String sku;
    private String name;
    private String description;
    private BigDecimal quantity;
    private String unit;
    private BigDecimal minimumStock;
    private BigDecimal reorderPoint;
    private BigDecimal maximumStock;
    private String location;
    private String category;
    private Map<String, String> attributes;
    private List<InventoryMovementDTO> recentMovements;
}