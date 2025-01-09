import {Component, Inject, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatButtonModule } from '@angular/material/button';
import {MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { DepartmentService } from '../../../core/services/department.service';
import { DepartmentDTO } from '../../../core/models/department.model';
import { Page } from '../../../core/models/user.model';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import {MatIcon} from '@angular/material/icon';
import {DepartmentDialogComponent} from './departaments-dialog/departaments-dialog.component';

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
  departments: DepartmentDTO[] = [];
  displayedColumns: string[] = ['code', 'name', 'description', 'managerId', 'actions'];
  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  loading = false;

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
          this.snackBar.open('Error loading departments', 'Close', { duration: 3000 });
          this.loading = false;
        }
      });
  }

  openDialog(department?: DepartmentDTO): void {
    const dialogRef = this.dialog.open(DepartmentDialogComponent, {
      width: '400px',
      data: department || {}
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
          this.snackBar.open('Department created successfully', 'Close', { duration: 3000 });
        },
        error: (error) => {
          this.snackBar.open('Error creating department', 'Close', { duration: 3000 });
        }
      });
  }

  updateDepartment(department: DepartmentDTO): void {
    this.departmentService.updateDepartment(department.id, department)
      .subscribe({
        next: () => {
          this.loadDepartments();
          this.snackBar.open('Department updated successfully', 'Close', { duration: 3000 });
        },
        error: (error) => {
          this.snackBar.open('Error updating department', 'Close', { duration: 3000 });
        }
      });
  }

  deleteDepartment(id: number): void {
    if (confirm('Are you sure you want to delete this department?')) {
      this.departmentService.deleteDepartment(id)
        .subscribe({
          next: () => {
            this.loadDepartments();
            this.snackBar.open('Department deleted successfully', 'Close', { duration: 3000 });
          },
          error: (error) => {
            this.snackBar.open('Error deleting department', 'Close', { duration: 3000 });
          }
        });
    }
  }

  onPageChange(event: any): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadDepartments();
  }
}


