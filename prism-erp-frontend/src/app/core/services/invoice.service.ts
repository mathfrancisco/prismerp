import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import {InvoiceDTO, InvoiceStatus, InvoiceTaxCalculationDTO} from '../models/invoice.model';
import { environment } from '../../../environment/environment';
import {Page} from '../models/user.model';


@Injectable({
  providedIn: 'root'
})
export class InvoiceService {
  private readonly API_URL = `${environment.apiUrl}/api/v1/invoices`;

  constructor(private http: HttpClient) {}

  generateInvoice(orderId: number): Observable<InvoiceDTO> {
    return this.http.post<InvoiceDTO>(`${this.API_URL}/generate/${orderId}`, {}).pipe( // {} para POST sem corpo
      catchError(this.handleError)
    );
  }

  getInvoiceById(id: number): Observable<InvoiceDTO> {
    return this.http.get<InvoiceDTO>(`${this.API_URL}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  updateInvoiceStatus(id: number, status: InvoiceStatus): Observable<InvoiceDTO> {
    const params = new HttpParams().set('status', status);
    return this.http.put<InvoiceDTO>(`${this.API_URL}/${id}/status`, {}, { params }).pipe(
      catchError(this.handleError)
    );
  }

  getAllInvoices(page: number = 0, size: number = 10): Observable<Page<InvoiceDTO>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Page<InvoiceDTO>>(this.API_URL, { params }).pipe(
      catchError(this.handleError)
    );
  }

  getInvoicesByStatus(status: InvoiceStatus, page: number = 0, size: number = 10): Observable<Page<InvoiceDTO>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Page<InvoiceDTO>>(`${this.API_URL}/status/${status}`, { params }).pipe(
      catchError(this.handleError)
    );
  }

  applyDiscount(id: number, discountPercentage: number): Observable<InvoiceDTO> {
    const params = new HttpParams().set('discountPercentage', discountPercentage.toString());
    return this.http.post<InvoiceDTO>(`${this.API_URL}/${id}/discount`, {}, { params }).pipe(
      catchError(this.handleError)
    );
  }

  calculateTaxes(id: number): Observable<InvoiceTaxCalculationDTO> {
    return this.http.get<InvoiceTaxCalculationDTO>(`${this.API_URL}/${id}/taxes`).pipe(
      catchError(this.handleError)
    );
  }

  getByInvoiceNumber(invoiceNumber: string): Observable<InvoiceDTO> {
    return this.http.get<InvoiceDTO>(`${this.API_URL}/number/${invoiceNumber}`).pipe(
      catchError(this.handleError)
    );
  }


  private handleError(error: HttpErrorResponse): Observable<never> {
    // ... (Implementação para tratamento de erros)
    let errorMessage = 'An error occurred';
    if (error.error instanceof ErrorEvent) {
      errorMessage = error.error.message;
    } else {
      errorMessage = error.error?.message || `Server returned code ${error.status}`;
    }
    return throwError(() => new Error(errorMessage));
  }
}
