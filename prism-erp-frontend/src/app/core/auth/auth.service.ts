// core/auth/auth.service.ts
import {Inject, Injectable, PLATFORM_ID} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {BehaviorSubject, Observable, of, tap, throwError} from 'rxjs';
import { Router } from '@angular/router';
import {AuthenticationRequest, ForgotPasswordRequest, ResetPasswordRequest} from '../models/user.model';
import {catchError} from 'rxjs/operators';
import {isPlatformBrowser} from '@angular/common';

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
    @Inject(PLATFORM_ID) private platformId: Object,
    private http: HttpClient,
    private router: Router
  ) {
    this.checkInitialAuth();
  }

  private checkInitialAuth(): void {
    if (isPlatformBrowser(this.platformId)){
    const token = localStorage.getItem('token');
    const user = localStorage.getItem('user');
    if (token && user) {
      this.isAuthenticatedSubject.next(true);
      this.currentUserSubject.next(JSON.parse(user));
    }}
  }


  authenticate(authRequest: AuthenticationRequest): Observable<AuthResponse | null>{
    console.log("Enviando requisição de autenticação:", authRequest); // Log antes da requisição
    return this.http.post<AuthResponse>(`${this.API_URL}/authenticate`, authRequest)
      .pipe(
        tap(response => {
          console.log("Resposta da API (authenticate):", response);
          if (response && response.token) { // Verifica se a resposta e o token existem
            localStorage.setItem('token', response.token);
            localStorage.setItem('user', JSON.stringify(response.user));
            this.isAuthenticatedSubject.next(true);
            this.currentUserSubject.next(response.user);
            console.log("Token e usuário armazenados no localStorage.");
          } else {
            console.error("Resposta da API não contém token ou está vazia.");
          }
        }),
        catchError(error => {
          console.error("Erro na requisição de autenticação:", error);
          this.handleError(error); // Chama this.handleError para tratamento e logs
          return of(null); // Retorna um observable vazio em caso de erro. Importante!
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

  resetPassword(request: ResetPasswordRequest): Observable<any> {
    return this.http.post(`${this.API_URL}/reset-password`, request).pipe( // Envia o objeto request
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
    let errorMessage = 'An unknown error occurred.'; // Mensagem padrão
    if (error.error instanceof ErrorEvent) {
      // Erro do lado do cliente
      errorMessage = `Client-side error: ${error.error.message}`;
    } else {
      // Erro do lado do servidor
      if (error.status !== 0) { // Verifica se houve resposta do servidor
        errorMessage = `Server returned code ${error.status}, error message is: ${error.message}`;
      } else {
        errorMessage = 'No response received from the server. Please check your internet connection.';
      }
    }

    console.error(errorMessage); // Log do erro no console
    // window.alert(errorMessage); // Alerta (opcional - remova se não quiser alertas)
    return throwError(() => new Error(errorMessage)); // Retorna um observable de erro
  }


}
