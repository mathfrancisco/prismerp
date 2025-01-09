import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { AuthService } from '../../../core/auth/auth.service';
import { UserDTO, Role } from '../../../core/models/user.model';
import { Router } from '@angular/router';
import { finalize } from 'rxjs/operators';
import {CommonModule} from '@angular/common';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';


@Component({
  selector: 'app-user-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FontAwesomeModule],
  templateUrl: './user-create.component.html',
  styleUrl: './user-create.component.scss'
})
export class UserCreateComponent {
  userForm: FormGroup;
  roles = Object.values(Role);
  isLoading = false;
  error: any;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.userForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      role: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.userForm.invalid) {
      return;
    }

    this.isLoading = true;
    const newUser: UserDTO = this.userForm.value;

    this.authService.createUser(newUser).pipe(
      finalize(() => this.isLoading = false)
    ).subscribe({
      next: () => {
        // Redireciona para a lista de usuários após a criação
        this.router.navigate(['/users']);
      },
      error: (error: any) => {
        this.error = error;
        // Trate o erro adequadamente, talvez exibindo uma mensagem de erro
      }
    });
  }
}
