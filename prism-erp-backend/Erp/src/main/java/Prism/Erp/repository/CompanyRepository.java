package Prism.Erp.repository;

import Prism.Erp.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    // Você pode adicionar métodos personalizados de consulta aqui se necessário
    boolean existsByDocumentNumber(String documentNumber);
}