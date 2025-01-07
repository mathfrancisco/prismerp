package Prism.Erp.controller;

import Prism.Erp.dto.EmployeeDTO;
import Prism.Erp.exception.ResourceNotFoundException;
import Prism.Erp.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(employeeDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employeeDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeDTO>> getAllEmployees(Pageable pageable) {
        return ResponseEntity.ok(employeeService.getAllEmployees(pageable));
    }

    @GetMapping("/employee-number/{employeeNumber}")
    public ResponseEntity<EmployeeDTO> getByEmployeeNumber(@PathVariable String employeeNumber) {
        return ResponseEntity.ok(employeeService.getByEmployeeNumber(employeeNumber));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<EmployeeDTO>> getByDepartmentId(@PathVariable Long departmentId) {
        return ResponseEntity.ok(employeeService.getEmployeesByDepartmentId(departmentId));
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleEmployeeNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // Adicione outros ExceptionHandlers para exceções personalizadas, como DepartmentNotFoundException
}
