import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService} from '../../../core/services/userservice';
import { UserDTO } from '../../../core/models/user.model';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { finalize } from 'rxjs/operators';
import { Role } from '../../../core/models/user.model'; // Importe o enum Role
import {CommonModule, Location, NgForOf} from '@angular/common';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome'; // Importe o Location

@Component({
  selector: 'app-user-detail',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FontAwesomeModule, ReactiveFormsModule, NgForOf], // Importe os módulos necessários
  templateUrl: './user-detail.component.html',
  styleUrl: './user-detail.component.scss'
})
export class UserDetailComponent implements OnInit {
  userId!: number;
  user!: UserDTO;
  userForm!: FormGroup;
  isLoading = false;
  roles = Object.values(Role); // Use Object.values para obter os valores do enum
  error: any;


  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private fb: FormBuilder,
    private location: Location // Injete o Location
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.userId = +params['id'];
      this.loadUser();
    });
  }

  loadUser(): void {
    this.isLoading = true;
    this.userService.getUserById(this.userId).pipe(
      finalize(() => this.isLoading = false)
    ).subscribe({
      next: (user: UserDTO) => {
        this.user = user;
        this.createForm();
      },
      error: (error: any) => {
        this.error = error;
        // Trate o erro adequadamente, talvez redirecionando ou exibindo uma mensagem
      }
    });
  }

  createForm(): void {
    this.userForm = this.fb.group({
      firstName: [this.user.firstName, Validators.required],
      lastName: [this.user.lastName, Validators.required],
      email: [this.user.email, [Validators.required, Validators.email]],
      role: [this.user.role, Validators.required]
    });
  }

  onSubmit(): void {
    if (this.userForm.invalid) {
      return;
    }

    this.isLoading = true;
    const updatedUser: UserDTO = {
      ...this.user,
      ...this.userForm.value
    };

    this.userService.updateUser(this.userId, updatedUser).pipe(
      finalize(() => this.isLoading = false)
    ).subscribe({
      next: (user: UserDTO) => {
        this.user = user;
        // Exiba uma mensagem de sucesso ou redirecione
        this.router.navigate(['/users']); // Redireciona para a lista de usuários
      },
      error: (error: any) => {
        this.error = error;
        // Trate o erro adequadamente
      }
    });
  }

  deleteUser(): void {
    // Implement delete logic here
  }

  goBack(): void {
    this.location.back(); // Usa o Location para voltar à página anterior
  }
}
