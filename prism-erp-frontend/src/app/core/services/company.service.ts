// company.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CompanyDTO } from '../models/company.model';
import { environment } from '../../../environment/environment';
import {catchError} from 'rxjs/operators';
import {HttpErrorResponse, HttpParams} from '@angular/common/http';
import {throwError} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CompanyService {
  private readonly API_URL = `${environment.apiUrl}/api/v1/companies`;

  constructor(private http: HttpClient) { }

  createCompany(company: CompanyDTO): Observable<CompanyDTO> {
    return this.http.post<CompanyDTO>(this.API_URL, company).pipe(
      catchError(this.handleError)
    );
  }

  getCompanyById(id: number): Observable<CompanyDTO> {
    return this.http.get<CompanyDTO>(`${this.API_URL}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  updateCompany(id: number, company: CompanyDTO): Observable<CompanyDTO> {
    return this.http.put<CompanyDTO>(`${this.API_URL}/${id}`, company).pipe(
      catchError(this.handleError)
    );
  }

  findActiveCompanies(): Observable<CompanyDTO[]> {
    return this.http.get<CompanyDTO[]>(`${this.API_URL}/active`).pipe(
      catchError(this.handleError)
    );
  }

  searchCompaniesByName(name: string): Observable<CompanyDTO[]> {
    const params = new HttpParams().set('name', name);
    return this.http.get<CompanyDTO[]>(`${this.API_URL}/search/name`, { params }).pipe(
      catchError(this.handleError)
    );
  }

  searchCompaniesByCity(city: string): Observable<CompanyDTO[]> {
    const params = new HttpParams().set('city', city);
    return this.http.get<CompanyDTO[]>(`${this.API_URL}/search/city`, { params }).pipe(
      catchError(this.handleError)
    );
  }

  searchCompaniesByState(state: string): Observable<CompanyDTO[]> {
    const params = new HttpParams().set('state', state);
    return this.http.get<CompanyDTO[]>(`${this.API_URL}/search/state`, { params }).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'An error occurred';
    if (error.error instanceof ErrorEvent) {
      errorMessage = error.error.message;
    } else {
      errorMessage = error.error?.message || `Server returned code ${error.status}`;
    }
    return throwError(() => new Error(errorMessage));
  }
}
