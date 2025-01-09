import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatIcon } from '@angular/material/icon';
import { DepartmentService } from '../../../core/services/department.service';
import { DepartmentDTO } from '../../../core/models/department.model';
import { Page } from '../../../core/models/user.model';
import { DepartmentDialogComponent } from './departaments-dialog/departaments-dialog.component'

@Component({
  selector: 'app-departments',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatButtonModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSnackBarModule,
    MatIcon
  ],
  templateUrl: './departments.component.html',
  styleUrls: ['./departments.component.scss']
})
export class DepartmentsComponent implements OnInit {
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  departments: DepartmentDTO[] = [];
  displayedColumns: string[] = ['code', 'name', 'description', 'managerId', 'actions'];
  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  loading = false;
  searchCode: string = '';
  selectedDepartment: DepartmentDTO | null = null;

  constructor(
    private departmentService: DepartmentService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadDepartments();
  }

  loadDepartments(): void {
    this.loading = true;
    this.departmentService.getAllDepartments(this.currentPage, this.pageSize)
      .subscribe({
        next: (page: Page<DepartmentDTO>) => {
          this.departments = page.content;
          this.totalElements = page.totalElements;
          this.loading = false;
        },
        error: (error) => {
          this.showError('Error loading departments');
          this.loading = false;
        }
      });
  }

  applyFilter(): void {
    if (this.searchCode) {
      this.departmentService.getDepartmentByCode(this.searchCode)
        .subscribe({
          next: (department) => {
            this.departments = department ? [department] : [];
            this.totalElements = this.departments.length;
          },
          error: () => {
            this.departments = [];
            this.totalElements = 0;
          }
        });
    } else {
      this.loadDepartments();
    }
  }

  openDialog(department?: DepartmentDTO): void {
    if (department) {
      this.departmentService.getDepartmentById(department.id)
        .subscribe({
          next: (fullDepartment) => {
            this.openDialogWithData(fullDepartment);
          },
          error: () => this.showError('Error loading department details')
        });
    } else {
      this.openDialogWithData({} as DepartmentDTO);
    }
  }

  private openDialogWithData(department: DepartmentDTO): void {
    const dialogRef = this.dialog.open(DepartmentDialogComponent, {
      width: '400px',
      data: department
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (result.id) {
          this.updateDepartment(result);
        } else {
          this.createDepartment(result);
        }
      }
    });
  }

  createDepartment(department: DepartmentDTO): void {
    this.departmentService.createDepartment(department)
      .subscribe({
        next: () => {
          this.loadDepartments();
          this.showSuccess('Department created successfully');
        },
        error: () => this.showError('Error creating department')
      });
  }

  updateDepartment(department: DepartmentDTO): void {
    this.departmentService.updateDepartment(department.id, department)
      .subscribe({
        next: () => {
          this.loadDepartments();
          this.showSuccess('Department updated successfully');
        },
        error: () => this.showError('Error updating department')
      });
  }

  deleteDepartment(id: number): void {
    if (confirm('Are you sure you want to delete this department?')) {
      this.departmentService.deleteDepartment(id)
        .subscribe({
          next: () => {
            this.loadDepartments();
            this.showSuccess('Department deleted successfully');
          },
          error: () => this.showError('Error deleting department')
        });
    }
  }

  onPageChange(event: any): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadDepartments();
  }

  private showSuccess(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      panelClass: ['success-snackbar']
    });
  }

  private showError(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      panelClass: ['error-snackbar']
    });
  }
}

