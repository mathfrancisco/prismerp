package Prism.Erp.service.impl;

import Prism.Erp.dto.AddressDTO;
import Prism.Erp.dto.CompanyDTO;
import Prism.Erp.service.AbstractCrudService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyServiceImpl<Company> extends AbstractCrudService<Company, CompanyDTO> {

    @Override
    protected CompanyDTO toDTO(Company entity) {
        return CompanyDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .documentNumber(entity.getDocumentNumber())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .address(convertAddressToDTO(entity.getAddress()))
                .active(entity.getActive())
                .build();
    }

    @Override
    protected String getEntityName() {
        return "Company";
    }

    private AddressDTO convertAddressToDTO(RabbitConnectionDetails.Address address) {
        if (address == null) return null;
        return AddressDTO.builder()
                .street(address.getStreet())
                .number(address.getNumber())
                .complement(address.getComplement())
                .neighborhood(address.getNeighborhood())
                .city(address.getCity())
                .state(address.getState())
                .zipCode(address.getZipCode())
                .build();
    }
}
