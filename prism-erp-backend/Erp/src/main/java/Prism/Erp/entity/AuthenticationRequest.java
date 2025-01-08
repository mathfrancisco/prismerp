package Prism.Erp.entity;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String email;
    private String password;

    // Getters
    public String getEmail() { return email; }
    public String getPassword() { return password; }
}