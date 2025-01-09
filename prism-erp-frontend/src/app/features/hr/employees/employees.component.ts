import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { EmployeeService } from '../../../core/services/employee.service';
import { EmployeeDTO, EmploymentStatus } from '../../../core/models/employee.model';
import { Page } from '../../../core/models/user.model';

@Component({
  selector: 'app-employees',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './employees.component.html',
  styleUrl: './employees.component.scss'
})
export class EmployeesComponent implements OnInit {
  employees: EmployeeDTO[] = [];
  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;
  loading = false;
  error: string | null = null;
  employmentStatuses = Object.values(EmploymentStatus);

  constructor(private employeeService: EmployeeService) {}

  ngOnInit(): void {
    this.loadEmployees();
  }

  loadEmployees(page: number = this.currentPage): void {
    this.loading = true;
    this.employeeService.getAllEmployees(page, this.pageSize)
      .subscribe({
        next: (response: Page<EmployeeDTO>) => {
          this.employees = response.content;
          this.totalElements = response.totalElements;
          this.totalPages = response.totalPages;
          this.currentPage = response.number;
          this.loading = false;
        },
        error: (error) => {
          this.error = 'Failed to load employees';
          this.loading = false;
          console.error('Error loading employees:', error);
        }
      });
  }

  onPageChange(page: number): void {
    if (page >= 0 && page < this.totalPages) {
    this.currentPage = page;
    this.loadEmployees(page);
  }
}

  deleteEmployee(id: number): void {
    if (confirm('Are you sure you want to delete this employee?')) {
    this.employeeService.deleteEmployee(id)
      .subscribe({
        next: () => {
          this.loadEmployees();
        },
        error: (error) => {
          this.error = 'Failed to delete employee';
          console.error('Error deleting employee:', error);
        }
      });
  }
}

  getStatusClass(status: EmploymentStatus): string {
    const statusClasses = {
      [EmploymentStatus.ACTIVE]: 'text-green-600 bg-green-100',
      [EmploymentStatus.ON_LEAVE]: 'text-yellow-600 bg-yellow-100',
      [EmploymentStatus.SUSPENDED]: 'text-red-600 bg-red-100',
      [EmploymentStatus.TERMINATED]: 'text-gray-600 bg-gray-100'
    };
    return statusClasses[status] || '';
  }
}
