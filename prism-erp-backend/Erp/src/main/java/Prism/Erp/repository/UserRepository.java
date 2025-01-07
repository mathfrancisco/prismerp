package Prism.Erp.repository;

import Prism.Erp.dto.UserDTO;
import Prism.Erp.entity.User;
import Prism.Erp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByRole(Role role);

    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "AND u.active = true")
    List<User> findByNameContaining(@Param("searchTerm") String searchTerm);

    @Query("SELECT u FROM User u WHERE u.active = true")
    List<User> findActiveUsers();

    @Query("SELECT u FROM User u WHERE u.role IN :roles AND u.active = true")
    List<User> findByRoles(@Param("roles") List<String> roles);

    @Query("SELECT u FROM User u WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :emailPattern, '%')) " +
            "AND u.active = true")
    List<User> findByEmailPattern(@Param("emailPattern") String emailPattern);

    // Additional security-focused query
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.active = true")
    Optional<User> findActiveUserByEmail(@Param("email") String email);


}