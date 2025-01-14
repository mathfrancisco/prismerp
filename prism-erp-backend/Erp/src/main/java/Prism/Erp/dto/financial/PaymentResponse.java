package Prism.Erp.dto.financial;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponse {
    private boolean success;
    private String transactionId;
    private String statusMessage;
    private LocalDateTime processedAt;
}
