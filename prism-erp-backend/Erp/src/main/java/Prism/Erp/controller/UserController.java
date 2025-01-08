package Prism.Erp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import Prism.Erp.dto.UserDTO;
import Prism.Erp.model.Role;
import Prism.Erp.service.UserService;

import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserDTO>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userDTO));
    }

    @GetMapping("/active")
    public ResponseEntity<List<UserDTO>> getActiveUsers() {
        return ResponseEntity.ok(userService.findActiveUsers());
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsersByName(@RequestParam String searchTerm) {
        return ResponseEntity.ok(userService.searchUsersByName(searchTerm));
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable Role role) {
        return ResponseEntity.ok(userService.findUsersByRole(role));
    }

    @GetMapping("/roles")
    public ResponseEntity<List<UserDTO>> getUsersByRoles(@RequestParam List<String> roles) {
        return ResponseEntity.ok(userService.findByRoles(roles));
    }

    @GetMapping("/email-pattern")
    public ResponseEntity<List<UserDTO>> getUsersByEmailPattern(@RequestParam String emailPattern) {
        return ResponseEntity.ok(userService.findByEmailPattern(emailPattern));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Autorização apenas para ADMIN
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.createUser(userDTO));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Autorização apenas para ADMIN
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
