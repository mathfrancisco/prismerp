import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { AuthService } from '../auth.service';
import { AuthenticationRequest } from '../../models/user.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit {
  returnUrl: string = '/dashboard';
  loginForm: FormGroup;
  isLoading = false;
  errorMessage: string | null = null;


  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.loginForm = this.formBuilder.group({
      email: ['', [
        Validators.required,
        Validators.email,
        Validators.pattern('^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$')
      ]],
      password: ['', [
        Validators.required,
        Validators.minLength(6)
      ]]
    });
  }

  ngOnInit(): void {

    // If already authenticated, redirect to home
    this.authService.isAuthenticated().subscribe(isAuthenticated => {
      if (isAuthenticated) {
        this.router.navigate([this.returnUrl]); // Use the correct returnUrl
      }
    });

    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/dashboard';
  }

  onSubmit(): void {
    if (this.loginForm.invalid || this.isLoading) {
      this.markFormFieldsAsTouched();
      return;
    }

    this.isLoading = true;
    this.errorMessage = null;

    const authRequest: AuthenticationRequest = {
      email: this.loginForm.value.email,
      password: this.loginForm.value.password
    };

    this.authService.authenticate(authRequest).subscribe({
      next: () => {
        this.router.navigate([this.returnUrl]);
      },
      error: (error: any) => {
        console.error('Login error:', error);
        this.errorMessage = this.getErrorMessage(error);
        this.isLoading = false;
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }

  private markFormFieldsAsTouched(): void {
    Object.keys(this.loginForm.controls).forEach(key => {
      const control = this.loginForm.get(key);
      control?.markAsTouched();
    });
  }

  private getErrorMessage(error: any): string {
    if (error.status === 401) {
      return 'Invalid email or password';
    } else if (error.status === 403) {
      return 'Account is locked. Please contact support';
    } else if (error.status === 0) {
      return 'Unable to connect to the server. Please check your internet connection';
    }
    return error.message || 'An unexpected error occurred. Please try again later';
  }

  getFieldError(fieldName: string): string {
    const control = this.loginForm.get(fieldName);

    if (control?.errors && control.touched) {
      if (control.errors['required']) {
        return `${fieldName.charAt(0).toUpperCase() + fieldName.slice(1)} is required`;
      }
      if (control.errors['email'] || control.errors['pattern']) {
        return 'Please enter a valid email address';
      }
      if (control.errors['minlength']) {
        return 'Password must be at least 6 characters long';
      }
    }
    return '';
  }

  resetError(): void {
    this.errorMessage = null;
  }
}
