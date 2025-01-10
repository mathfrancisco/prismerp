import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { AuthService } from '../auth.service';
import {RouterLink} from '@angular/router';
import {CommonModule, NgIf} from '@angular/common';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [
    RouterLink,
    ReactiveFormsModule,
    CommonModule,
    NgIf
  ],
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.scss'
})
export class ForgotPasswordComponent {
  forgotPasswordForm: FormGroup;
  isLoading = false;
  isSuccess = false;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService
  ) {
    this.forgotPasswordForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  onSubmit(): void {
    if (this.forgotPasswordForm.invalid || this.isLoading) {
      return;
    }

    this.isLoading = true;
    const { email } = this.forgotPasswordForm.value;

    this.authService.forgotPassword(email).subscribe({
      next: () => {
        this.isSuccess = true;
        this.forgotPasswordForm.reset();
      },
      error: (error) => {
        console.error('Password reset request error:', error);
        this.isLoading = false;
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }
  getFieldError(fieldName: string): string {
    const control = this.forgotPasswordForm.get(fieldName);
    if (control?.touched && control.invalid) {
      if (control.hasError('required')) {
        return `${fieldName} is required.`;
      }
      if (control.hasError('email')) {
        return 'Please enter a valid email address.';
      }
    }
    return '';
  }
}
