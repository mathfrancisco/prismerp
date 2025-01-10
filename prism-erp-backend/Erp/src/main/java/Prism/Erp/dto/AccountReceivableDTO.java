package Prism.Erp.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class AccountReceivableDTO {
    private Long id;
    private String documentNumber;
    private Long customerId;
    private String customerName;
    private Long invoiceId;
    private BigDecimal amount;
    private LocalDate dueDate;
    private String status;
    private LocalDate paymentDate;
    private BigDecimal paidAmount;
    private BigDecimal remainingAmount;
    private String paymentMethod;
}