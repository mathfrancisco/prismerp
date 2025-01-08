package Prism.Erp.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 255, message = "O nome pode ter no máximo 255 caracteres")
    private String name;

    @NotBlank(message = "O número do documento é obrigatório")
    @Pattern(regexp = "\\d{14}", message = "O número do documento deve ter 14 dígitos") // Exemplo de regex para CNPJ
    private String documentNumber;


    @Email(message = "Email inválido")
    private String email;

    private String phone;

    @Valid
    private AddressDTO address;

    @NotNull(message = "O status ativo/inativo é obrigatório")
    private Boolean active;
}