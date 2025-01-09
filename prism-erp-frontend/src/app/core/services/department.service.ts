import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { DepartmentDTO } from '../models/department.model';
import { environment } from '../../../environment/environment';
import {Page} from '../models/user.model';


@Injectable({
  providedIn: 'root'
})
export class DepartmentService {
  private readonly API_URL = `${environment.apiUrl}/api/v1/departments`;

  constructor(private http: HttpClient) {}

  createDepartment(department: DepartmentDTO): Observable<DepartmentDTO> {
    return this.http.post<DepartmentDTO>(this.API_URL, department).pipe(
      catchError(this.handleError)
    );
  }

  getDepartmentById(id: number): Observable<DepartmentDTO> {
    return this.http.get<DepartmentDTO>(`${this.API_URL}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  updateDepartment(id: number, department: DepartmentDTO): Observable<DepartmentDTO> {
    return this.http.put<DepartmentDTO>(`${this.API_URL}/${id}`, department).pipe(
      catchError(this.handleError)
    );
  }

  deleteDepartment(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  getAllDepartments(page: number = 0, size: number = 10): Observable<Page<DepartmentDTO>> { // Paginação
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Page<DepartmentDTO>>(this.API_URL, { params }).pipe(
      catchError(this.handleError)
    );
  }

  getAllDepartmentsList(): Observable<DepartmentDTO[]> { // Para obter todos os departamentos sem paginação
    return this.http.get<DepartmentDTO[]>(`${this.API_URL}/all`).pipe(
      catchError(this.handleError)
    );
  }


  getDepartmentByCode(code: string): Observable<DepartmentDTO> {
    return this.http.get<DepartmentDTO>(`${this.API_URL}/code/${code}`).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    // Implementação de tratamento de erros (igual ao exemplo anterior)
    let errorMessage = 'An error occurred';
    if (error.error instanceof ErrorEvent) {
      errorMessage = error.error.message;
    } else {
      errorMessage = error.error?.message || `Server returned code ${error.status}`;
    }
    return throwError(() => new Error(errorMessage));
  }
}
