package Prism.Erp.service.impl;


import Prism.Erp.dto.UserDTO;
import Prism.Erp.model.Role;
import Prism.Erp.entity.User;
import Prism.Erp.repository.UserRepository;
import Prism.Erp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    @Override
    public List<UserDTO> findUsersByRole(Role role) {
        return userRepository.findByRole(role).stream().map(this::convertToDTO).toList();
    }

    @Override
    public List<UserDTO> searchUsersByName(String searchTerm) {
        return userRepository.findByNameContaining(searchTerm).stream().map(this::convertToDTO).toList();
    }
    @Override
    public List<UserDTO> findActiveUsers() {
        return userRepository.findActiveUsers().stream().map(this::convertToDTO).toList();
    }
    @Override
    public List<UserDTO> findByRoles(List<String> roles) {
        return userRepository.findByRoles(roles).stream().map(this::convertToDTO).toList();
    }
    @Override
    public List<UserDTO> findByEmailPattern(String emailPattern) {
        return userRepository.findByEmailPattern(emailPattern).stream().map(this::convertToDTO).toList();
    }

    @Override
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }
    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        // Validações
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists.");
        }
        // ... outras validações que você precisar

        User user = convertFromDTO(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Codifica a senha
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        // Exclusão lógica (recomendado)
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        user.setActive(false); // Define o usuário como inativo
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        updateUserFromDTO(user, userDTO);
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    // Implementação do método convertFromDTO
    private User convertFromDTO(UserDTO userDTO) {
        return User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword()) // Adicione o campo de senha
                .role(Role.valueOf(userDTO.getRole()))
                 // Defina como ativo por padrão, se aplicável
                .build();
    }

    private void updateUserFromDTO(User user, UserDTO userDTO) {
        if (userDTO.getFirstName() != null) {
            user.setFirstName(userDTO.getFirstName());
        }
        if (userDTO.getLastName() != null) {
            user.setLastName(userDTO.getLastName());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getRole() != null) {
            user.setRole(Role.valueOf(userDTO.getRole().toUpperCase()));
        }
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRole(user.getRole().name());
        return dto;
    }

}