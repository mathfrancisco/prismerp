package Prism.Erp.dto.business.edi;

import Prism.Erp.model.business.EdiStatus;
import Prism.Erp.model.business.EdiTransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EdiTransactionDTO {
    private Long id;
    private String referenceNumber;
    private EdiTransactionType type;
    private EdiStatus status;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
}
