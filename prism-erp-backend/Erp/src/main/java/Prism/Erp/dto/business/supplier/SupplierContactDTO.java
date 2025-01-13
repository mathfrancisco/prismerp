package Prism.Erp.dto.business.supplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierContactDTO {
    private Long id;
    private String contactType;
    private String description;
    private LocalDateTime contactDate;
    private String contactedBy;
    private String outcome;
}