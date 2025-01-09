// employee.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { EmployeeDTO } from '../models/employee.model';
import { environment } from '../../../environment/environment';
import {Page} from '../models/user.model';


@Injectable({
  providedIn: 'root'
})
export class EmployeeService {
  private readonly API_URL = `${environment.apiUrl}/api/v1/employees`;

  constructor(private http: HttpClient) {}

  createEmployee(employee: EmployeeDTO): Observable<EmployeeDTO> {
    return this.http.post<EmployeeDTO>(this.API_URL, employee).pipe(
      catchError(this.handleError)
    );
  }

  getEmployeeById(id: number): Observable<EmployeeDTO> {
    return this.http.get<EmployeeDTO>(`${this.API_URL}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  updateEmployee(id: number, employee: EmployeeDTO): Observable<EmployeeDTO> {
    return this.http.put<EmployeeDTO>(`${this.API_URL}/${id}`, employee).pipe(
      catchError(this.handleError)
    );
  }

  deleteEmployee(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  getAllEmployees(page: number = 0, size: number = 10): Observable<Page<EmployeeDTO>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<Page<EmployeeDTO>>(this.API_URL, { params }).pipe(
      catchError(this.handleError)
    );
  }

  getEmployeeByEmployeeNumber(employeeNumber: string): Observable<EmployeeDTO> {
    return this.http.get<EmployeeDTO>(`${this.API_URL}/employee-number/${employeeNumber}`).pipe(
      catchError(this.handleError)
    );
  }

  getEmployeesByDepartmentId(departmentId: number): Observable<EmployeeDTO[]> {
    return this.http.get<EmployeeDTO[]>(`${this.API_URL}/department/${departmentId}`).pipe(
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
