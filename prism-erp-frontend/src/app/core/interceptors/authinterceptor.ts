// auth.interceptor.ts
import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { isPlatformBrowser } from '@angular/common';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(@Inject(PLATFORM_ID) private platformId: any) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (isPlatformBrowser(this.platformId)) {
      const token = localStorage.getItem('token');
      if (token) {
        req = req.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`
          }
        });
      }
    }
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          // Lógica para lidar com erro 401 (redirecionar para login, etc.)
          console.error("Erro 401: Não autorizado");
          return throwError(() => error); // Ou você pode retornar um observable vazio: return EMPTY;
        }
        return throwError(() => error);
      })
    );
  }
  modifyRequest(req: HttpRequest<any>): HttpRequest<any> {
    if (isPlatformBrowser(this.platformId)) {
      const token = localStorage.getItem('token');
      if (token) {
        return req.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`,
          },
        });
      }
    }
    return req;
  }

  handleError(error: HttpErrorResponse): Observable<HttpEvent<any>> {
    if (error.status === 401) {
      console.error('Error 401: Unauthorized');
    }
    return throwError(() => error);
  }
}
