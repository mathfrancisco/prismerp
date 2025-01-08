package Prism.Erp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DepartmentDTO {

    private Long id;

    @NotBlank(message = "O nome do departamento é obrigatório")
    private String name;

    @NotBlank(message = "O código do departamento é obrigatório")
    private String code;

    private Long managerId; // ID do gerente

    private String description;
}
