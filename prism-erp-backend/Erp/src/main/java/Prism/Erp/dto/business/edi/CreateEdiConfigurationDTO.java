package Prism.Erp.dto.business.edi;

import Prism.Erp.model.business.EdiFormat;
import Prism.Erp.model.business.EdiProtocol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEdiConfigurationDTO {
    @NotNull
    private Long supplierId;

    @NotNull
    private EdiFormat format;

    @NotNull
    private EdiProtocol protocol;

    @NotBlank
    private String endpoint;

    private Map<String, String> credentials;

    private Map<String, String> mappings;

    private String certificatePath;
}
