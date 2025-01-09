// core/auth/auth.service.ts
import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {BehaviorSubject, Observable, tap, throwError} from 'rxjs';
import { Router } from '@angular/router';
import {AuthenticationRequest, ForgotPasswordRequest, ResetPasswordRequest} from '../models/user.model';
import {catchError} from 'rxjs/operators';

interface AuthResponse {
  token: string;
  user: {
    id: number;
    email: string;
    firstName: string; // Use firstName
    lastName: string;  // Use lastName
    role: string;      // Use role
  };
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = 'http://localhost:8080/api/v1/auth';
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);
  private currentUserSubject = new BehaviorSubject<any>(null);

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    this.checkInitialAuth();
  }

  private checkInitialAuth(): void {
    const token = localStorage.getItem('token');
    const user = localStorage.getItem('user');
    if (token && user) {
      this.isAuthenticatedSubject.next(true);
      this.currentUserSubject.next(JSON.parse(user));
    }
  }

  authenticate(authRequest: AuthenticationRequest): Observable<AuthResponse> { // Renomeado para authenticate
    return this.http.post<AuthResponse>(`${this.API_URL}/authenticate`, authRequest)
      .pipe(
        tap(response =>{
          localStorage.setItem('token', response.token);
          localStorage.setItem('user', JSON.stringify(response.user));
          this.isAuthenticatedSubject.next(true);
          this.currentUserSubject.next(response.user);
        })
      );
  }

  register(userData: any): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/register`, userData);
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.isAuthenticatedSubject.next(false);
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  forgotPassword(request: ForgotPasswordRequest): Observable<any> { // Use o tipo correto
    return this.http.post(`${this.API_URL}/forgot-password`, request).pipe(
      catchError(this.handleError)
    );
  }

  resetPassword( request: ResetPasswordRequest): Observable<any> { // Use o tipo correto
    return this.http.post(`${this.API_URL}/reset-password`, request).pipe(
      catchError(this.handleError)
    );
  }

  isAuthenticated(): Observable<boolean> {
    return this.isAuthenticatedSubject.asObservable();
  }

  getCurrentUser(): Observable<any> {
    return this.currentUserSubject.asObservable();

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
