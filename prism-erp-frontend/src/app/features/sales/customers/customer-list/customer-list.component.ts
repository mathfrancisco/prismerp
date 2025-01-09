// customer-list.component.ts
import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import { CustomerService } from '../../../../core/services/customer.service';
import { CustomerDTO } from '../../../../core/models/customer.model';
import { finalize } from 'rxjs/operators';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-customer-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './customer-list.component.html',
  styleUrls: ['./customer-list.component.scss']
})
export class CustomerListComponent implements OnInit {
  customers: CustomerDTO[] = [];
  isLoading = false;
  error: string | null = null;
  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;
  searchTerm: string = '';
  @Output() selectCustomer = new EventEmitter<CustomerDTO>();


  constructor(private customerService: CustomerService) {}

  ngOnInit(): void {
    this.loadCustomers();
  }

  loadCustomers(): void {
    this.isLoading = true;
    this.customerService.getCustomers(this.currentPage, this.pageSize, this.searchTerm).pipe(
      finalize(() => this.isLoading = false)
    ).subscribe({
      next: (response) => {
        this.customers = response.content;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
      },
      error: (error) => {
        this.error = 'Error loading customers.';
        console.error('Error loading customers:', error);
      }
    });
  }

  deleteCustomer(customer: CustomerDTO): void {
    if (confirm(`Are you sure you want to delete ${customer.name}?`)) {
      this.customerService.deleteCustomer(customer.id!).subscribe({
        next: () => this.loadCustomers(),
        error: (error) => {
          console.error('Error deleting customer:', error);
          // Lide com o erro apropriadamente, ex: exibindo uma mensagem de erro
        }
      });
    }
  }

  onPageChange(newPage: number): void {
    this.currentPage = newPage;
    this.loadCustomers();
  }

  onSearchTermChange(newSearchTerm: string): void {
    this.searchTerm = newSearchTerm;
    this.loadCustomers();
  }
  getPages(): number[] {
    return Array(this.totalPages).fill(0).map((x, i) => i);
  }
}

