import { enableProdMode, importProvidersFrom, inject, PLATFORM_ID } from '@angular/core';
import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { environment } from './environment/environment'; // Caminho relativo corrigido
import { provideRouter } from '@angular/router';
import { routes } from './app/app.routes';

import {
  provideHttpClient,
  withInterceptors,
  withFetch,
  HttpInterceptorFn,
  HttpRequest,
  HttpHandlerFn,
  HttpEvent,
  HttpErrorResponse,
} from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { isPlatformBrowser } from '@angular/common';
import { catchError, Observable, throwError } from 'rxjs';

if (environment.production) {
  enableProdMode();
}

const authInterceptor: HttpInterceptorFn = (req: HttpRequest<any>, next: HttpHandlerFn) => {
  if (isPlatformBrowser(inject(PLATFORM_ID))) { // Injete PLATFORM_ID aqui
    const token = localStorage.getItem('token');
    if (token) {
      req = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
      });
    }
  }
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        console.error('Error 401: Unauthorized');
      }
      return throwError(() => error);
    })
  );
};

bootstrapApplication(AppComponent, { // Removido 'ApplicationConfig='
  providers: [
    provideRouter(routes),
    importProvidersFrom(BrowserAnimationsModule),
    provideHttpClient(withFetch(), withInterceptors([authInterceptor])),
  ],
}).catch((err) => console.error(err));

