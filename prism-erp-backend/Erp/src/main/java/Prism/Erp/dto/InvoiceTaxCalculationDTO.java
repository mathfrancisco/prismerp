package Prism.Erp.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Builder
@Getter
@Setter
public class InvoiceTaxCalculationDTO {
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private BigDecimal icmsAmount;
    private BigDecimal pisAmount;
    private BigDecimal cofinsAmount;
    private BigDecimal issAmount;
}