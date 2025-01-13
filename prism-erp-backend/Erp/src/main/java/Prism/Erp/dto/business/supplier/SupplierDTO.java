package Prism.Erp.dto.business.supplier;

import Prism.Erp.model.business.SupplierStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDTO {
    private Long id;
    private String name;
    private String documentNumber;
    private String contactName;
    private String email;
    private String phone;
    private SupplierStatus status;
    private String address;
    private String website;
    private String bankDetails;
    private Integer paymentTerms;
    private String taxRegime;
    private BigDecimal creditLimit;
    private BigDecimal qualityRating;
    private BigDecimal deliveryPerformance;
    private LocalDate lastEvaluationDate;
    private Set<String> categories;
    private Set<String> certifications;
    private List<SupplierDocumentDTO> documents;
    private List<SupplierContactDTO> contactHistory;
}