package Prism.Erp.dto;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private String token;
    private UserDTO user;
}
