package Prism.Erp.service;

import Prism.Erp.dto.CompanyDTO;

import java.util.List;

public interface CompanyService {
    CompanyDTO createCompany(CompanyDTO companyDTO);
    CompanyDTO getCompanyById(Long id);
    CompanyDTO updateCompany(Long id, CompanyDTO companyDTO);
    List<CompanyDTO> findActiveCompanies();
    List<CompanyDTO> findByNameContaining(String name);
    List<CompanyDTO> findByCityContaining(String city);
    List<CompanyDTO> findByState(String state);
    // ... outros métodos que você precisar
}