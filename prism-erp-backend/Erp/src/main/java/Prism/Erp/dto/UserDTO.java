package Prism.Erp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor  // Adicione se você precisar de um construtor sem argumentos
@AllArgsConstructor // Adicione se você precisar de um construtor com todos os argumentos
public class UserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
}
