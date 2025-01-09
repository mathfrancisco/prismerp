// customer.service.ts
import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import { Observable } from 'rxjs';
import { CustomerDTO } from '../models/customer.model';
import {environment} from '../../../environment/environment'; // Importe o modelo
import { Page } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private readonly API_URL = `${environment.apiUrl}/api/v1/customers`;

  constructor(private http: HttpClient) {}

  getCustomers(page: number, size: number, searchTerm?: string): Observable<Page<CustomerDTO>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (searchTerm) {
      params = params.set('search', searchTerm);
    }

    return this.http.get<Page<CustomerDTO>>(this.API_URL, { params });
  }

  getCustomerById(id: number): Observable<CustomerDTO> {
    return this.http.get<CustomerDTO>(`${this.API_URL}/${id}`);
  }

  createCustomer(customer: CustomerDTO): Observable<CustomerDTO> {
    return this.http.post<CustomerDTO>(this.API_URL, customer);
  }

  updateCustomer(id: number, customer: CustomerDTO): Observable<CustomerDTO> {
    return this.http.put<CustomerDTO>(`${this.API_URL}/${id}`, customer);
  }

  deleteCustomer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
