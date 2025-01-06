package Prism.Erp.service.impl;

import Prism.Erp.dto.UserDTO;
import Prism.Erp.entity.User;
import Prism.Erp.repository.UserRepository;
import Prism.Erp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::convertToDTO);
    }

    @Override
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Atualize o usuário com as informações do DTO
        user.setFirstName(userDTO.getFirstName());
        // ... outros campos

        return convertToDTO(userRepository.save(user));
    }

    private UserDTO convertToDTO(User user) {
        // ... (Implementação da conversão para DTO)
    }


    private User convertToUser(UserDTO userDTO) {
        return User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .role(userDTO.getRole()) // Certifique-se de que o tipo de função seja mapeado corretamente
                .build();
    }
}