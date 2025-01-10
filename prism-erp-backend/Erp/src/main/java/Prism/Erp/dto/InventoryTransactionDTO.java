package Prism.Erp.dto;

import Prism.Erp.model.TransactionType;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InventoryTransactionDTO {
    private Long id;
    private Long productId;
    private String productName;
    private String type;
    private Integer quantity;
    private String reference;
    private String notes;
    private LocalDateTime transactionDate;
    private String createdBy;
    private TransactionType transactionType;  // Changed from String type
    private Integer previousQuantity;
    private Integer newQuantity;
    private Long referenceId;


}