package Prism.Erp.service;

import Prism.Erp.dto.DepartmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepartmentService {
    DepartmentDTO createDepartment(DepartmentDTO departmentDTO);
    DepartmentDTO getDepartmentById(Long id);
    DepartmentDTO updateDepartment(Long id, DepartmentDTO departmentDTO);
    void deleteDepartment(Long id);
    Page<DepartmentDTO> getAllDepartments(Pageable pageable);
    List<DepartmentDTO> getAllDepartments(); // Adicionado este
    DepartmentDTO getByCode(String code);
    // ... outros métodos que você precisar
}
