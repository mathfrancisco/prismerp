import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { InventoryTransactionDTO, ProductStockDTO } from '../models/inventory-transaction.model';
import { environment } from '../../../environment/environment';
import {Page} from '../models/user.model';


@Injectable({
  providedIn: 'root'
})
export class InventoryService {
  private readonly API_URL = `${environment.apiUrl}/api/v1/inventory`;

  constructor(private http: HttpClient) {}

  createTransaction(transaction: InventoryTransactionDTO): Observable<InventoryTransactionDTO> {
    return this.http.post<InventoryTransactionDTO>(`${this.API_URL}/transactions`, transaction).pipe(
      catchError(this.handleError)
    );
  }

  getStockLevels(page: number = 0, size: number = 10): Observable<Page<ProductStockDTO>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Page<ProductStockDTO>>(`${this.API_URL}/stock-levels`, { params }).pipe(
      catchError(this.handleError)
    );
  }

  getLowStockProducts(): Observable<ProductStockDTO[]> {
    return this.http.get<ProductStockDTO[]>(`${this.API_URL}/low-stock`).pipe(
      catchError(this.handleError)
    );
  }

  getTransactionsByProductId(productId: number): Observable<InventoryTransactionDTO[]> {
    return this.http.get<InventoryTransactionDTO[]>(`${this.API_URL}/transactions/product/${productId}`).pipe(
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
