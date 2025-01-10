package Prism.Erp.dto;

import Prism.Erp.entity.Invoice;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InvoiceDTO extends Invoice {
    private Long id;
    private String invoiceNumber;
    private Long salesOrderId;
    private BigDecimal totalAmount;
    private String status;
    private LocalDate invoiceDate;
    private LocalDate dueDate;
}
