package Prism.Erp.service.impl;

import Prism.Erp.dto.DepartmentDTO;
import Prism.Erp.entity.Department;
import Prism.Erp.entity.Employee;
import Prism.Erp.exception.ResourceNotFoundException;
import Prism.Erp.repository.DepartmentRepository;
import Prism.Erp.repository.EmployeeRepository;
import Prism.Erp.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        Department department = convertToEntity(departmentDTO);
        return convertToDTO(departmentRepository.save(department));
    }

    @Override
    public DepartmentDTO getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento não encontrado com o ID: " + id));
    }

    @Override
    public DepartmentDTO updateDepartment(Long id, DepartmentDTO departmentDTO) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento não encontrado com o ID: " + id));

        updateEntityFromDTO(department, departmentDTO);
        return convertToDTO(departmentRepository.save(department));
    }

    @Override
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    @Override
    public Page<DepartmentDTO> getAllDepartments(Pageable pageable) {
        return departmentRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    @Override
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentDTO getByCode(String code) {
        return departmentRepository.findByCode(code)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento não encontrado com o código: " + code));
    }


    private DepartmentDTO convertToDTO(Department department) {
        return DepartmentDTO.builder()
                .id(department.getId())
                .name(department.getName())
                .code(department.getCode())
                .managerId(department.getManager() != null ? department.getManager().getId() : null)
                .description(department.getDescription())
                .build();
    }

    private Department convertToEntity(DepartmentDTO departmentDTO) {
        Employee manager = null;
        if (departmentDTO.getManagerId() != null) {
            manager = employeeRepository.findById(departmentDTO.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Gerente não encontrado com o ID: " + departmentDTO.getManagerId()));
        }

        return Department.builder()
                .name(departmentDTO.getName())
                .code(departmentDTO.getCode())
                .manager(manager)
                .description(departmentDTO.getDescription())
                .build();
    }

    private void updateEntityFromDTO(Department department, DepartmentDTO departmentDTO) {
        department.setName(Optional.ofNullable(departmentDTO.getName()).orElse(department.getName()));
        department.setCode(Optional.ofNullable(departmentDTO.getCode()).orElse(department.getCode()));
        department.setDescription(Optional.ofNullable(departmentDTO.getDescription()).orElse(department.getDescription()));


        if (departmentDTO.getManagerId() != null) {
            Employee manager = employeeRepository.findById(departmentDTO.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Gerente não encontrado com o ID: " + departmentDTO.getManagerId()));
            department.setManager(manager);
        } else {
            department.setManager(null);
        }
    }
}
