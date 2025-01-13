package Prism.Erp.dto.business.supplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDocumentDTO {
    private Long id;
    private String documentType;
    private String documentNumber;
    private LocalDate expirationDate;
    private String status;
    private String fileUrl;
    private LocalDateTime uploadDate;
}