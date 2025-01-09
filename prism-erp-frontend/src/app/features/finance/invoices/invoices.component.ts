// invoices.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button'; // Importe o MatButtonModule
import { InvoiceService } from '../../../core/services/invoice.service';
import { InvoiceDTO, InvoiceStatus, InvoiceTaxCalculationDTO } from '../../../core/models/invoice.model';
import { Page } from '../../../core/models/user.model';
import {MatMenu, MatMenuItem, MatMenuTrigger} from '@angular/material/menu';
import {MatProgressSpinner} from '@angular/material/progress-spinner';

@Component({
  selector: 'app-invoices',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, ReactiveFormsModule, MatMenuTrigger, MatMenu, MatMenuItem, MatButtonModule, MatProgressSpinner],
  templateUrl: './invoices.component.html',
  styleUrls: ['./invoices.component.scss']
})
export class InvoicesComponent implements OnInit {
  invoices: InvoiceDTO[] = [];
  selectedInvoice: InvoiceDTO | null = null;
  taxCalculation: InvoiceTaxCalculationDTO | null = null;
  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;
  loading = false;
  error: string | null = null;
  success: string | null = null;
  filterStatus: InvoiceStatus | '' = '';
  invoiceStatuses = Object.values(InvoiceStatus);
  discountForm: FormGroup;

  constructor(
    private invoiceService: InvoiceService,
    private fb: FormBuilder
  ) {
    this.discountForm = this.fb.group({
      discountPercentage: ['']
    });
  }

  ngOnInit(): void {
    this.loadInvoices();
  }

  loadInvoices(page: number = this.currentPage): void {
    this.loading = true;
    const request = this.filterStatus
      ? this.invoiceService.getInvoicesByStatus(this.filterStatus, page, this.pageSize)
      : this.invoiceService.getAllInvoices(page, this.pageSize);

    request.subscribe({
      next: (response: Page<InvoiceDTO>) => {
        this.invoices = response.content;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        this.currentPage = response.number;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load invoices';
        this.loading = false;
        console.error('Error loading invoices:', error);
      }
    });
  }

  generateInvoice(orderId: number): void {
    this.loading = true;
    this.invoiceService.generateInvoice(orderId).subscribe({
      next: (invoice) => {
        this.success = `Invoice ${invoice.invoiceNumber} generated successfully`;
        this.loadInvoices();
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to generate invoice';
        this.loading = false;
      }
    });
  }

  updateStatus(invoice: InvoiceDTO, newStatus: string): void {
    this.loading = true;
    this.invoiceService.updateInvoiceStatus(invoice.id, newStatus).subscribe({
      next: (updatedInvoice) => {
        this.success = `Invoice status updated to ${newStatus}`;
        const index = this.invoices.findIndex(i => i.id === updatedInvoice.id);
        if (index !== -1) {
          this.invoices[index] = updatedInvoice;
        }
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to update invoice status';
        this.loading = false;
      }
    });
  }

  applyDiscount(invoice: InvoiceDTO): void {
    const discountPercentage = this.discountForm.get('discountPercentage')?.value;
    if (discountPercentage !== null && discountPercentage !== '') {
      this.loading = true;
      this.invoiceService.applyDiscount(invoice.id, discountPercentage).subscribe({
        next: (updatedInvoice) => {
          this.success = `Discount applied successfully`;
          const index = this.invoices.findIndex(i => i.id === updatedInvoice.id);
          if (index !== -1) {
            this.invoices[index] = updatedInvoice;
          }
          this.discountForm.reset();
          this.loading = false;
        },
        error: (error) => {
          this.error = 'Failed to apply discount';
          this.loading = false;
        }
      });
    }
  }

  calculateTaxes(invoice: InvoiceDTO): void {
    this.loading = true;
    this.invoiceService.calculateTaxes(invoice.id).subscribe({
      next: (taxCalculation) => {
        this.taxCalculation = taxCalculation;
        this.selectedInvoice = invoice;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to calculate taxes';
        this.loading = false;
      }
    });
  }

  onPageChange(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadInvoices(page);
    }
  }

  onStatusFilterChange(status: string | null): void { // Aceite string | null
    this.filterStatus = status === '' || status === null ? '' : status as InvoiceStatus; // Converta para InvoiceStatus se válido
    this.currentPage = 0;
    this.loadInvoices(0);
  }

  getStatusClass(status: InvoiceStatus): string {
    const statusClasses = {
      [InvoiceStatus.DRAFT]: 'text-gray-600 bg-gray-100',
      [InvoiceStatus.ISSUED]: 'text-blue-600 bg-blue-100',
      [InvoiceStatus.PAID]: 'text-green-600 bg-green-100',
      [InvoiceStatus.PARTIALLY_PAID]: 'text-yellow-600 bg-yellow-100',
      [InvoiceStatus.OVERDUE]: 'text-red-600 bg-red-100',
      [InvoiceStatus.CANCELLED]: 'text-purple-600 bg-purple-100',
      [InvoiceStatus.VOID]: 'text-gray-600 bg-gray-100'
    };
    return statusClasses[status] || '';
  }

  clearMessages(): void {
    this.error = null;
    this.success = null;
  }
}
