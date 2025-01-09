import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import { CompanyService } from '../../../core/services/company.service';
import { CompanyDTO } from '../../../core/models/company.model';

@Component({
  selector: 'app-company-form',
  templateUrl: './company-form.component.html',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  styleUrls: ['./company-form.component.scss']
})
export class CompanyFormComponent implements OnInit {
  companyForm: FormGroup;
  isEditMode = false;
  loading = false;
  error: string | null = null;
  companyId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private companyService: CompanyService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.companyForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      documentNumber: ['', [Validators.required, Validators.pattern(/^\d{14}$/)]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern(/^\d{10,11}$/)]],
      address: this.fb.group({
        street: ['', Validators.required],
        number: ['', Validators.required],
        complement: [''],
        neighborhood: ['', Validators.required],
        city: ['', Validators.required],
        state: ['', Validators.required],
        zipCode: ['', [Validators.required, Validators.pattern(/^\d{8}$/)]],
      })
    });
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.companyId = parseInt(id, 10);
      this.loadCompany(this.companyId);
    }
  }

  private loadCompany(id: number): void {
    this.loading = true;
    this.companyService.getCompanyById(id).subscribe({
      next: (company) => {
        this.companyForm.patchValue(company);
        this.loading = false;
      },
      error: (error) => {
        this.error = error.message;
        this.loading = false;
      }
    });
  }

  onSubmit(): void {
    if (this.companyForm.invalid) {
      return;
    }

    this.loading = true;
    const company: CompanyDTO = this.companyForm.value;

    const request = this.isEditMode && this.companyId
      ? this.companyService.updateCompany(this.companyId, company)
      : this.companyService.createCompany(company);

    request.subscribe({
      next: () => {
        this.router.navigate(['/companies']);
      },
      error: (error) => {
        this.error = error.message;
        this.loading = false;
      }
    });
  }
}
