package Prism.Erp.dto.financial;

import Prism.Erp.model.financial.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class AccountPayableDTO {
    private Long id;
    private String documentNumber;
    private BigDecimal amount;
    private LocalDate dueDate;
    private LocalDate issueDate;
    private PaymentStatus status;
    private String description;
    private Long supplierId;
    private List<PaymentScheduleDTO> schedules;
    private Long costCenterId;
}
