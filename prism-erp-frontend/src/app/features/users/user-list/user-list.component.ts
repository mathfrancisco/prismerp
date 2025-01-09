import { Component, OnInit } from '@angular/core';
import { UserService} from '../../../core/services/userservice';
import { UserDTO } from '../../../core/models/user.model';
import {FormControl, ReactiveFormsModule} from '@angular/forms';
import {debounceTime, distinctUntilChanged, finalize, switchMap} from 'rxjs/operators';
import {CommonModule, NgForOf, NgIf} from '@angular/common';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgForOf,
    CommonModule,
    NgIf
  ],
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.scss'
})
export class UserListComponent implements OnInit {
  users: UserDTO[] = [];
  roles = ['ADMIN', 'USER', 'MANAGER', 'ACCOUNTANT', 'SALES_REPRESENTATIVE'];
  searchControl = new FormControl('');
  roleFilter = new FormControl('');
  isAdmin = true; // You should get this from your auth service

  // Pagination
  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;

  get startIndex(): number {
    return this.currentPage * this.pageSize;
  }

  get endIndex(): number {
    return Math.min((this.currentPage + 1) * this.pageSize, this.totalElements);
  }

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.loadUsers();

    this.searchControl.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe(() => {
      this.loadUsers();
    });

    this.roleFilter.valueChanges.subscribe(() => {
      this.loadUsers();
    });
  }

  loadUsers(): void {
    const searchTerm = this.searchControl.value;
    const role = this.roleFilter.value;

    if (searchTerm) {
      this.userService.searchUsers(searchTerm).subscribe(users => {
        this.users = users;
      });
    } else if (role) {
      this.userService.getUsersByRole(role).subscribe(users => {
        this.users = users;
      });
    } else {
      this.userService.getUsers(this.currentPage, this.pageSize).subscribe(page => {
        this.users = page.content;
        this.totalElements = page.totalElements;
        this.totalPages = page.totalPages;
      });
    }
  }

  changePage(page: number): void {
    this.currentPage = page;
    this.loadUsers();
  }

  editUser(userDTO: UserDTO): void {
    // Implement edit logic
  }

  deleteUser(user: UserDTO): void {
    if (confirm(`Are you sure you want to delete ${user.firstName} ${user.lastName}?`)) {
      this.userService.deleteUser(user.id).pipe(
        finalize(() => this.loadUsers()), // Recarrega a lista após a exclusão
        switchMap(() => this.userService.getUsers(this.currentPage, this.pageSize))
      ).subscribe({
        next: (page) => {
          this.users = page.content;
          this.totalElements = page.totalElements;
          this.totalPages = page.totalPages;
        },
        error: (error) => {
          // Lide com o erro, exiba uma mensagem, etc.
          console.error('Error deleting user:', error);
        }
      });
    }
  }
}
