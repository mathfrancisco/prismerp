// src/app/features/company/company-list/company-list.component.ts
import { Component, OnInit } from '@angular/core';
import { CompanyService } from '../../../core/services/company.service';
import { CompanyDTO } from '../../../core/models/company.model';
import {FormControl, ReactiveFormsModule} from '@angular/forms';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-company-list',
  templateUrl: './company-list.component.html',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    RouterLink
  ],
  styleUrls: ['./company-list.component.scss']
})
export class CompanyListComponent implements OnInit {
  companies: CompanyDTO[] = [];
  searchControl = new FormControl('');
  searchType = 'name'; // 'name' | 'city' | 'state'
  loading = false;
  error: string | null = null;

  constructor(private companyService: CompanyService) { }

  ngOnInit(): void {
    this.loadActiveCompanies();
    this.setupSearch();
  }

  private loadActiveCompanies(): void {
    this.loading = true;
    this.companyService.findActiveCompanies()
      .subscribe({
        next: (data) => {
          this.companies = data;
          this.loading = false;
        },
        error: (error) => {
          this.error = error.message;
          this.loading = false;
        }
      });
  }

  private setupSearch(): void {
    this.searchControl.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(value => {
        if (!value) return this.companyService.findActiveCompanies();

        switch (this.searchType) {
          case 'name':
            return this.companyService.searchCompaniesByName(value);
          case 'city':
            return this.companyService.searchCompaniesByCity(value);
          case 'state':
            return this.companyService.searchCompaniesByState(value);
          default:
            return this.companyService.findActiveCompanies();
        }
      })
    ).subscribe({
      next: (data) => this.companies = data,
      error: (error) => this.error = error.message
    });
  }

  setSearchType(type: string): void {
    this.searchType = type;
    if (this.searchControl.value) {
      this.searchControl.updateValueAndValidity();
    }
  }
}
