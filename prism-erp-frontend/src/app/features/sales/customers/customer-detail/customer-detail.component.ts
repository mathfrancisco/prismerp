// customer-detail.component.ts
import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, RouterLink} from '@angular/router';
import { CustomerService } from '../../../../core/services/customer.service';
import { CustomerDTO } from '../../../../core/models/customer.model';
import {CommonModule, Location} from '@angular/common';
import { finalize, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-customer-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './customer-detail.component.html',
  styleUrls: ['./customer-detail.component.scss']
})
export class CustomerDetailComponent implements OnInit {
  customer: CustomerDTO | undefined;
  isLoading = false;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private customerService: CustomerService,
    private location: Location // Para voltar à página anterior
  ) {}

  ngOnInit(): void {
    this.getCustomer();
  }

  getCustomer(): void {
    this.isLoading = true;
    this.route.paramMap.pipe(
      switchMap(params => {
        const id = Number(params.get('id'));
        return this.customerService.getCustomerById(id);
      }),
      finalize(() => this.isLoading = false)
    ).subscribe({
      next: (customer) => this.customer = customer,
      error: (error) => {
        this.error = 'Error loading customer.';
        console.error('Error loading customer:', error);
      }
    });
  }

  goBack(): void {
    this.location.back();
  }
}
