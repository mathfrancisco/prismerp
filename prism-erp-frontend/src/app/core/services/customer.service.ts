// customer.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CustomerDTO } from '../models/customer.model'; // Importe o modelo

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private readonly API_URL = 'http://localhost:8080/api/customers'; // URL da sua API

  constructor(private http: HttpClient) {}

  getCustomers(page: number, size: number, searchTerm: string = ''): Observable<any> {
    let url = `${this.API_URL}?page=${page}&size=${size}`;
    if (searchTerm) {
      url += `&searchTerm=${searchTerm}`; // Adiciona o termo de busca à URL
    }
    return this.http.get<any>(url);
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

  getByDocumentNumber(documentNumber: string): Observable<CustomerDTO> {
    return this.http.get<CustomerDTO>(`${this.API_URL}/document/${documentNumber}`);
  }
}
