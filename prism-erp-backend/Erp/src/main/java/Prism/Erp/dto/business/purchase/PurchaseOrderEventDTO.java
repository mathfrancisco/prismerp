package Prism.Erp.dto.business.purchase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderEventDTO {
    private LocalDateTime timestamp;
    private String event;
    private String description;
    private String username;
    private Map<String, Object> changes;
}