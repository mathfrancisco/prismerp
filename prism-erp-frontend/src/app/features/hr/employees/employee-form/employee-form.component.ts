import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { EmployeeService } from '../../../../core/services/employee.service';
import { EmployeeDTO, EmploymentStatus } from '../../../../core/models/employee.model';
import { DepartmentDTO } from '../../../../core/models/department.model';
import { DepartmentService } from '../../../../core/services/department.service';

@Component({
  selector: 'app-employee-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './employee-form.component.html',
  styleUrl: './employee-form.component.scss'
})
export class EmployeeFormComponent implements OnInit {
  employeeForm!: FormGroup; // Using the definite assignment assertion
  isEditMode = false;
  loading = false;
  error: string | null = null;
  departments: DepartmentDTO[] = [];
  employmentStatuses = Object.values(EmploymentStatus);

  constructor(
    private fb: FormBuilder,
    private employeeService: EmployeeService,
    private departmentService: DepartmentService,
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.initForm(); // Initializing in constructor
  }

  ngOnInit(): void {
    this.loadDepartments();
    this.checkForExistingEmployee();
  }

  private initForm(): void {
    this.employeeForm = this.fb.group({
      employeeNumber: ['', Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', Validators.required],
      joinDate: ['', Validators.required],
      status: [EmploymentStatus.ACTIVE, Validators.required],
      departmentId: ['', Validators.required],
      salary: ['', Validators.required]
    });
  }

  private loadDepartments(): void {
    this.departmentService.getAllDepartmentsList().subscribe({
      next: (departments) => this.departments = departments,
      error: (error) => {
        this.error = 'Failed to load departments';
        console.error('Error loading departments:', error);
      }
    });
  }

  private checkForExistingEmployee(): void {
    const id = this.route.snapshot.params['id'];
    if (id) {
      this.isEditMode = true;
      this.loading = true;
      this.employeeService.getEmployeeById(id).subscribe({
        next: (employee) => {
          this.employeeForm.patchValue({
            ...employee,
            joinDate: this.formatDate(employee.joinDate)
          });
          this.loading = false;
        },
        error: (error) => {
          this.error = 'Failed to load employee details';
          this.loading = false;
          console.error('Error loading employee:', error);
        }
      });
    }
  }

  onSubmit(): void {
    if (this.employeeForm.valid) {
      this.loading = true;
      const employeeData = this.employeeForm.value;

      if (this.isEditMode) {
        const id = this.route.snapshot.params['id'];
        this.updateEmployee(id, employeeData);
      } else {
        this.createEmployee(employeeData);
      }
    }
  }

  private createEmployee(employeeData: EmployeeDTO): void {
    this.employeeService.createEmployee(employeeData).subscribe({
      next: () => {
        this.showSuccess('Employee created successfully');
        this.router.navigate(['/employees']);
      },
      error: (error) => {
        this.handleError(error);
      }
    });
  }

  private updateEmployee(id: number, employeeData: EmployeeDTO): void {
    this.employeeService.updateEmployee(id, employeeData).subscribe({
      next: () => {
        this.showSuccess('Employee updated successfully');
        this.router.navigate(['/employees']);
      },
      error: (error) => {
        this.handleError(error);
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/employees']);
  }

  private formatDate(date: string): string {
    return new Date(date).toISOString().split('T')[0];
  }

  private showSuccess(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      panelClass: ['success-snackbar']
    });
  }

  private handleError(error: any): void {
    this.loading = false;
    this.error = error.message || 'An error occurred';
  }
}
