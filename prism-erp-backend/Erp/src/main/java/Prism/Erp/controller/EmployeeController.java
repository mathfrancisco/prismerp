package Prism.Erp.controller;

import Prism.Erp.dto.EmployeeDTO;
import Prism.Erp.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    // ... outros endpoints (create, get, update, delete, getAll)

    @GetMapping("/employee-number/{employeeNumber}")
    public ResponseEntity<EmployeeDTO> getByEmployeeNumber(@PathVariable String employeeNumber) {
        return ResponseEntity.ok(employeeService.getByEmployeeNumber(employeeNumber));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<EmployeeDTO>> getByDepartmentId(@PathVariable Long departmentId) {
        return ResponseEntity.ok(employeeService.getEmployeesByDepartmentId(departmentId));
    }
}
