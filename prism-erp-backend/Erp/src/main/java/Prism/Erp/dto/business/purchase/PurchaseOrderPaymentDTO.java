package Prism.Erp.dto.business.purchase;

import Prism.Erp.model.business.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderPaymentDTO {
    private Long id;
    private BigDecimal amount;
    private LocalDate dueDate;
    private PaymentStatus status;
    private String paymentMethod;
    private String bankDetails;
    private String notes;
}
