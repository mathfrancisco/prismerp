package Prism.Erp.service;

import Prism.Erp.dto.UserDTO;
import Prism.Erp.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    Page<UserDTO> getAllUsers(Pageable pageable);
    UserDTO getUserById(Long id);
    UserDTO updateUser(Long id, UserDTO userDTO);
    List<UserDTO> findUsersByRole(Role role);
    List<UserDTO> searchUsersByName(String searchTerm);
    List<UserDTO> findActiveUsers();
    List<UserDTO> findByRoles(List<String> roles);
    List<UserDTO> findByEmailPattern(String emailPattern);
}