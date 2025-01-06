package Prism.Erp.service;

import Prism.Erp.dto.CompanyDTO;

public interface CompanyService {
    CompanyDTO createCompany(CompanyDTO companyDTO);
    CompanyDTO getCompanyById(Long id);
    CompanyDTO updateCompany(Long id, CompanyDTO companyDTO);
    // ... outros métodos que você precisar
}