package Prism.Erp.controller;

import Prism.Erp.dto.CompanyDTO;
import Prism.Erp.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<CompanyDTO> createCompany(@Valid @RequestBody CompanyDTO companyDTO) {
        return ResponseEntity.ok(companyService.createCompany(companyDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getCompany(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyDTO> updateCompany(
            @PathVariable Long id,
            @Valid @RequestBody CompanyDTO companyDTO) {
        return ResponseEntity.ok(companyService.updateCompany(id, companyDTO));
    }
    @GetMapping("/active")
    public ResponseEntity<List<CompanyDTO>> findActiveCompanies() {
        return ResponseEntity.ok(companyService.findActiveCompanies());
    }
    @GetMapping("/search/name")
    public ResponseEntity<List<CompanyDTO>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(companyService.findByNameContaining(name));
    }

    @GetMapping("/search/city")
    public ResponseEntity<List<CompanyDTO>> searchByCity(@RequestParam String city) {
        return ResponseEntity.ok(companyService.findByCityContaining(city));
    }

    @GetMapping("/search/state")
    public ResponseEntity<List<CompanyDTO>> searchByState(@RequestParam String state) {
        return ResponseEntity.ok(companyService.findByState(state));
    }
}
