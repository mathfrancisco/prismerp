package Prism.Erp.service.impl;

import Prism.Erp.dto.EmployeeDTO;
import Prism.Erp.entity.Department;
import Prism.Erp.entity.Employee;

import Prism.Erp.exception.ResourceNotFoundException;
import Prism.Erp.repository.DepartmentRepository;
import Prism.Erp.repository.EmployeeRepository;
import Prism.Erp.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository; // Certifique-se de injetar o DepartmentRepository


    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = convertToEntity(employeeDTO);
        return convertToDTO(employeeRepository.save(employee));
    }

    @Override
    public EmployeeDTO getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com o ID: " + id));
    }

    @Override
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com o ID: " + id));

        updateEntityFromDTO(employee, employeeDTO);
        return convertToDTO(employeeRepository.save(employee));
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public Page<EmployeeDTO> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    @Override
    public EmployeeDTO getByEmployeeNumber(String employeeNumber) {
        return employeeRepository.findByEmployeeNumber(employeeNumber)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado com o número: " + employeeNumber));
    }

    @Override
    public List<EmployeeDTO> getEmployeesByDepartmentId(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    private EmployeeDTO convertToDTO(Employee employee) {
        return EmployeeDTO.builder()
                .id(employee.getId())
                .employeeNumber(employee.getEmployeeNumber())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .joinDate(employee.getJoinDate())
                .status(employee.getStatus())
                .departmentId(employee.getDepartment() != null ? employee.getDepartment().getId() : null) // Extrai o departmentId
                .salary(employee.getSalary())
                .build();
    }

    private Employee convertToEntity(EmployeeDTO employeeDTO) {
        Department department = null;
        if (employeeDTO.getDepartmentId() != null) {
            department = departmentRepository.findById(employeeDTO.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Departamento não encontrado")); // Trate a exceção adequadamente
        }

        return Employee.builder()
                .employeeNumber(employeeDTO.getEmployeeNumber())
                .firstName(employeeDTO.getFirstName())
                .lastName(employeeDTO.getLastName())
                .email(employeeDTO.getEmail())
                .phone(employeeDTO.getPhone())
                .joinDate(employeeDTO.getJoinDate())
                .status(employeeDTO.getStatus())
                .department(department) // Define o departamento
                .salary(employeeDTO.getSalary())
                .build();
    }

    private void updateEntityFromDTO(Employee employee, EmployeeDTO employeeDTO) {
        // Use o operador ternário ou atribuição condicional (Java 14+) para simplificar
        employee.setEmployeeNumber(employeeDTO.getEmployeeNumber() != null ? employeeDTO.getEmployeeNumber() : employee.getEmployeeNumber());
        employee.setFirstName(employeeDTO.getFirstName() != null ? employeeDTO.getFirstName() : employee.getFirstName());
        // ... outros campos

        if (employeeDTO.getDepartmentId() != null) {
            Department department = departmentRepository.findById(employeeDTO.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Departamento não encontrado")); // Trate a exceção adequadamente
            employee.setDepartment(department);
        } else {
            employee.setDepartment(null);
        }
    }
}
