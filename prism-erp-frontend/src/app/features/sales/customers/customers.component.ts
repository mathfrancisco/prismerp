// src/app/features/sales/customers/customers.component.ts
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CustomerListComponent } from './customer-list/customer-list.component';
import {CustomerFormComponent} from './customer-form/customer-form.component';
import {CustomerDetailComponent} from './customer-detail/customer-detail.component';
import {CustomerDTO} from '../../../core/models/customer.model';
import {NgIf} from '@angular/common';


@Component({
  selector: 'app-customers',
  standalone: true,
  imports: [
    CustomerListComponent,
    CustomerFormComponent,
    CustomerDetailComponent,
    NgIf
  ],
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.scss'
})
export class CustomersComponent {
  selectedCustomer: CustomerDTO | null = null;
  showForm = false;
  showDetail = false;
  private _customer: CustomerDTO | undefined;

  onSelectCustomer(customer: CustomerDTO): void {
    this.selectedCustomer = customer;
    this.showDetail = true;
    this.showForm = false;
  }

  onNewCustomer(): void {
    this.selectedCustomer = null;
    this.showForm = true;
    this.showDetail = false;
  }

  onCustomerSubmit(customer: CustomerDTO): void {
    this._customer = customer;
    // Aqui você pode adicionar lógica após salvar o customer
    this.showForm = false;
    this.showDetail = false;
  }

  onBackToList(): void {
    this.showForm = false;
    this.showDetail = false;
    this.selectedCustomer = null;
  }
}
