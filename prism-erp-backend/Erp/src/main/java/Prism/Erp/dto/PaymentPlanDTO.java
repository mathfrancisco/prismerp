package Prism.Erp.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class PaymentPlanDTO {
    private Long id;
    private Long invoiceId;
    private Integer numberOfInstallments;
    private BigDecimal installmentAmount;
    private String frequency;
    private LocalDate firstDueDate;
    private BigDecimal interestRate;
    private String paymentMethod;
}
