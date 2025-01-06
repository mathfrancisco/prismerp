package Prism.Erp.entity;

import Prism.Erp.dto.UserDTO;
import lombok.Data;

@Data
public class AuthenticationResponse {
    private String token;
    private UserDTO user;


}
