package Prism.Erp.service.impl;

import Prism.Erp.dto.AddressDTO;
import Prism.Erp.dto.CompanyDTO;
import Prism.Erp.entity.Company;
import Prism.Erp.model.Address;
import Prism.Erp.repository.CompanyRepository;
import Prism.Erp.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Override
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        Company company = convertToEntity(companyDTO);
        return convertToDTO(companyRepository.save(company));
    }

    @Override
    public CompanyDTO getCompanyById(Long id) {
        return companyRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada")); // Use uma exceção mais específica se possível
    }

    @Override
    public CompanyDTO updateCompany(Long id, CompanyDTO companyDTO) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

        updateEntityFromDTO(company, companyDTO);
        return convertToDTO(companyRepository.save(company));

    }


    private CompanyDTO convertToDTO(Company company) {
        AddressDTO addressDTO = null;
        if (company.getAddress() != null) {
            addressDTO = AddressDTO.builder()
                    .street(company.getAddress().getStreet())
                    .number(company.getAddress().getNumber())
                    .complement(company.getAddress().getComplement())
                    .neighborhood(company.getAddress().getNeighborhood())
                    .city(company.getAddress().getCity())
                    .state(company.getAddress().getState())
                    .zipCode(company.getAddress().getZipCode())
                    .build();
        }

        return CompanyDTO.builder()
                .id(company.getId())
                .name(company.getName())
                .documentNumber(company.getDocumentNumber())
                .email(company.getEmail())
                .phone(company.getPhone())
                .address(addressDTO)
                .active(company.getActive())
                .build();
    }

    private Company convertToEntity(CompanyDTO companyDTO) {
        Address address = null;
        if (companyDTO.getAddress() != null) {
            address = Address.builder()
                    .street(companyDTO.getAddress().getStreet())
                    .number(companyDTO.getAddress().getNumber())
                    .complement(companyDTO.getAddress().getComplement())
                    .neighborhood(companyDTO.getAddress().getNeighborhood())
                    .city(companyDTO.getAddress().getCity())
                    .state(companyDTO.getAddress().getState())
                    .zipCode(companyDTO.getAddress().getZipCode())
                    .build();
        }

        return Company.builder()
                .name(companyDTO.getName())
                .documentNumber(companyDTO.getDocumentNumber())
                .email(companyDTO.getEmail())
                .phone(companyDTO.getPhone())
                .address(address)
                .active(companyDTO.getActive())
                .build();
    }


    private void updateEntityFromDTO(Company company, CompanyDTO companyDTO) {
        company.setName(companyDTO.getName());
        company.setDocumentNumber(companyDTO.getDocumentNumber());
        company.setEmail(companyDTO.getEmail());
        company.setPhone(companyDTO.getPhone());
        company.setActive(companyDTO.getActive());

        if (companyDTO.getAddress() != null) {
            Address address = company.getAddress();
            if (address == null) {
                address = new Address();
                company.setAddress(address);
            }
            address.setStreet(companyDTO.getAddress().getStreet());
            address.setNumber(companyDTO.getAddress().getNumber());
            address.setComplement(companyDTO.getAddress().getComplement());
            address.setNeighborhood(companyDTO.getAddress().getNeighborhood());
            address.setCity(companyDTO.getAddress().getCity());
            address.setState(companyDTO.getAddress().getState());
            address.setZipCode(companyDTO.getAddress().getZipCode());
        } else {
            company.setAddress(null);
        }
    }
}