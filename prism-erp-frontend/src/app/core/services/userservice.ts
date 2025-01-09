import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, finalize, retry, tap } from 'rxjs/operators';
import { environment } from '../../../environment/environment';
import { Page, UserDTO } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly API_URL = `${environment.apiUrl}/api/users`; // URL base para o backend

  constructor(private http: HttpClient) {}

  getUsers(page: number = 0, size: number = 10): Observable<Page<UserDTO>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<Page<UserDTO>>(`${this.API_URL}`, { params }).pipe(
      retry(3), // Tenta novamente 3 vezes em caso de erro
      catchError(this.handleError)
    );
  }

  getUserById(id: number): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.API_URL}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  createUser(user: UserDTO): Observable<UserDTO> {
    return this.http.post<UserDTO>(`${this.API_URL}`, user).pipe(
      catchError(this.handleError)
    );
  }

  updateUser(id: number, user: UserDTO): Observable<UserDTO> {
    return this.http.put<UserDTO>(`${this.API_URL}/${id}`, user).pipe(
      catchError(this.handleError)
    );
  }

  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  searchUsers(searchTerm: string): Observable<UserDTO[]> {
    const params = new HttpParams().set('search', searchTerm);
    return this.http.get<UserDTO[]>(`${this.API_URL}/search`, { params }).pipe(
      catchError(this.handleError)
    );
  }

  getUsersByRole(role: string): Observable<UserDTO[]> {
    const params = new HttpParams().set('role', role);
    return this.http.get<UserDTO[]>(`${this.API_URL}/role`, { params }).pipe(
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
