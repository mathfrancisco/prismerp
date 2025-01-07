package Prism.Erp.service;

import Prism.Erp.dto.EmployeeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);
    EmployeeDTO getEmployeeById(Long id);
    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);
    void deleteEmployee(Long id);
    Page<EmployeeDTO> getAllEmployees(Pageable pageable);
    EmployeeDTO getByEmployeeNumber(String employeeNumber);
    List<EmployeeDTO> getEmployeesByDepartmentId(Long departmentId);
    // ... outros métodos que você precisar
}
