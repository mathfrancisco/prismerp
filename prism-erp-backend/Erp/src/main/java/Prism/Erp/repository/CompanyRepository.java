package Prism.Erp.repository;

import Prism.Erp.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    // Você pode adicionar métodos personalizados de consulta aqui se necessário
    boolean existsByDocumentNumber(String documentNumber);
    List<Company> findByActiveTrue();
    List<Company> findByNameContainingIgnoreCase(String name);
    List<Company> findByAddressCityContainingIgnoreCase(String city);
    List<Company> findByAddressStateIgnoreCase(String state);
}