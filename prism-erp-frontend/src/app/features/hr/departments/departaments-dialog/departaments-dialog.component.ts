import {Component, Inject} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from '@angular/material/dialog';
import {DepartmentDTO} from '../../../../core/models/department.model';

@Component({
  selector: 'app-department-dialog',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatDialogModule
  ],
  template: `
    <h2 mat-dialog-title>{{data.id ? 'Edit' : 'Create'}} Department</h2>
    <form [formGroup]="form" (ngSubmit)="onSubmit()">
      <div mat-dialog-content>
        <mat-form-field class="w-full">
          <mat-label>Name</mat-label>
          <input matInput formControlName="name" required>
        </mat-form-field>

        <mat-form-field class="w-full">
          <mat-label>Code</mat-label>
          <input matInput formControlName="code" required>
        </mat-form-field>

        <mat-form-field class="w-full">
          <mat-label>Description</mat-label>
          <input matInput formControlName="description">
        </mat-form-field>

        <mat-form-field class="w-full">
          <mat-label>Manager ID</mat-label>
          <input matInput type="number" formControlName="managerId">
        </mat-form-field>
      </div>

      <div mat-dialog-actions align="end">
        <button mat-button (click)="onCancel()">Cancel</button>
        <button mat-raised-button color="primary" type="submit" [disabled]="!form.valid">
          {{data.id ? 'Update' : 'Create'}}
        </button>
      </div>
    </form>
  `
})
export class DepartmentDialogComponent {
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<DepartmentDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DepartmentDTO
  ) {
    this.form = this.fb.group({
      name: [data.name || '', [Validators.required, Validators.minLength(3)]],
      code: [data.code || '', [Validators.required, Validators.pattern('^[A-Z0-9]+$')]],
      description: [data.description || ''],
      managerId: [data.managerId || null]
    });
  }

  onSubmit(): void {
    if (this.form.valid) {
      this.dialogRef.close({
        ...this.form.value,
        id: this.data.id
      });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
