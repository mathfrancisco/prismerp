package Prism.Erp.dto.financial;

import Prism.Erp.dto.business.supplier.SupplierInfo;
import Prism.Erp.model.financial.PaymentType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class PaymentRequest {
    private BigDecimal amount;
    private LocalDate scheduledDate;
    private PaymentType paymentType;
    private String bankAccount;
    private SupplierInfo supplierInfo;
    private String documentNumber;
    private String description;
}
