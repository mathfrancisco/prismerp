package Prism.Erp.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SalesOrderDTO {
    private Long id;
    private String orderNumber;
    private Long customerId;
    private String customerName;
    private List<SalesOrderItemDTO> items;
    private String status;
    private BigDecimal totalAmount;
    private LocalDate orderDate;
    private LocalDate deliveryDate;
}
