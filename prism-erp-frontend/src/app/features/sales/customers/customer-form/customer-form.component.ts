// customer-form.component.ts
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { CustomerDTO, Address } from '../../../../core/models/customer.model';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-customer-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './customer-form.component.html',
  styleUrls: ['./customer-form.component.scss']
})
export class CustomerFormComponent implements OnInit {
  @Input() customer: CustomerDTO | null = null;
  @Output() customerSubmit = new EventEmitter<CustomerDTO>();
  @Output() cancel = new EventEmitter<void>();
  customerForm!: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.createForm();
    if (this.customer) {
      this.patchForm(this.customer);
    }
  }

  createForm(): void {
    this.customerForm = this.fb.group({
      name: ['', Validators.required],
      documentNumber: ['', Validators.required],
      email: ['', [Validators.email]],
      phone: [''],
      address: this.fb.group({
        street: ['', Validators.required],
        number: ['', Validators.required],
        complement: [''],
        zipCode: ['', Validators.required],
        city: ['', Validators.required],
        state: ['', Validators.required],
        country: ['', Validators.required]
      }),
      active: [true]
    });
  }

  patchForm(customer: CustomerDTO): void {
    this.customerForm.patchValue({
      name: customer.name,
      documentNumber: customer.documentNumber,
      email: customer.email,
      phone: customer.phone,
      address: customer.address,
      active: customer.active
    });
  }

  onSubmit(): void {
    if (this.customerForm.valid) {
      this.customerSubmit.emit(this.customerForm.value);
    }
  }
}
