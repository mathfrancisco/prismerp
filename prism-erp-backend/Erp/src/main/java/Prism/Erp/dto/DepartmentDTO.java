package Prism.Erp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO {

    private Long id;

    @NotBlank(message = "O nome do departamento é obrigatório")
    private String name;

    @NotBlank(message = "O código do departamento é obrigatório")
    private String code;

    private Long managerId; // ID do gerente

    private String description;
}
