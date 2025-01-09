import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CustomerDTO } from '../../../../core/models/customer.model';
import { CommonModule } from '@angular/common';
import { CustomerService } from '../../../../core/services/customer.service';
import { ActivatedRoute, Router } from '@angular/router';

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
  loading = false;
  error: string | null = null;
  isEditMode = false;
  customerId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private customerService: CustomerService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.createForm();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.customerId = parseInt(id, 10);
      this.loadCustomer(this.customerId);
    } else if (this.customer) {
      this.patchForm(this.customer);
    }
  }

  createForm(): void {
    this.customerForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      documentNumber: ['', [Validators.required, Validators.pattern(/^\d{11}|\d{14}$/)]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern(/^\d{10,11}$/)]],
      address: this.fb.group({
        street: ['', Validators.required],
        number: ['', Validators.required],
        complement: [''],
        zipCode: ['', [Validators.required, Validators.pattern(/^\d{8}$/)]],
        city: ['', Validators.required],
        state: ['', Validators.required],
        country: ['', Validators.required]
      }),
      active: [true]
    });
  }

  loadCustomer(id: number): void {
    this.loading = true;
    this.customerService.getCustomerById(id).subscribe({
      next: (customer) => {
        this.patchForm(customer);
        this.loading = false;
      },
      error: (error) => {
        this.error = error.message;
        this.loading = false;
      }
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

  async onSubmit(): Promise<void> {
    if (this.customerForm.invalid || this.loading) {
      return;
    }

    this.loading = true;
    const customerData: CustomerDTO = this.customerForm.value;

    try {
      if (this.isEditMode && this.customerId) {
        await this.customerService.updateCustomer(this.customerId, customerData).toPromise();
      } else {
        await this.customerService.createCustomer(customerData).toPromise();
      }
      this.router.navigate(['/customers']);
    } catch (error: any) {
      this.error = error.message;
    } finally {
      this.loading = false;
    }
  }
}
