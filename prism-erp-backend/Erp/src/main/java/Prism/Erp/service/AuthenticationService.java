package Prism.Erp.service;

import Prism.Erp.entity.AuthenticationRequest;
import Prism.Erp.entity.AuthenticationResponse;
import Prism.Erp.dto.UserDTO;
import Prism.Erp.entity.RegisterRequest;
import Prism.Erp.model.Role;
import Prism.Erp.entity.User;
import Prism.Erp.repository.UserRepository;
import Prism.Erp.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;



    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
        var token = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .user(convertToUserDTO(user))
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws AuthenticationException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthenticationException("Invalid credentials"));

        var token = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .user(convertToUserDTO(user))
                .build();
    }

    private UserDTO convertToUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .build();
    }

    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        String token = UUID.randomUUID().toString(); // Gere um token aleatório
        user.setPasswordResetToken(token); // Salva o token no usuário
        userRepository.save(user);

        // TODO: Enviar email para o usuário com o link de redefinição de senha
        // O link deve conter o token gerado, por exemplo: /reset-password?token={token}
        log.info("Password reset token generated for user {}: {}", email, token);

    }

    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByPasswordResetToken(token) // Corrigido
                .orElseThrow(() -> new IllegalArgumentException("Invalid password reset token"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null); // Limpa o token após o uso
        userRepository.save(user);

        log.info("Password successfully reset for user {}", user.getEmail());
    }
}
