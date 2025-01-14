package Prism.Erp.dto.financial;

import Prism.Erp.model.financial.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class AccountPayableDTO {
    private Long id;
    private String documentNumber;
    private BigDecimal amount;
    private LocalDate dueDate;
    private LocalDate issueDate;
    private PaymentStatus status;
    private String description;
    private Long supplierId;
    private Long costCenterId;
    private List<PaymentScheduleDTO> schedules;
}

