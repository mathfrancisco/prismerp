package Prism.Erp.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    private String name;

    @NotBlank(message = "O número do documento é obrigatório")
    private String documentNumber;

    @Email(message = "Email inválido")
    private String email;

    private String phone;

    @Valid
    private AddressDTO address;

    @NotNull(message = "O status ativo/inativo é obrigatório")
    private Boolean active;
}