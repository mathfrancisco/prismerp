import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable, of } from 'rxjs';
import { AuthService } from '../auth/auth.service';
import { isPlatformBrowser } from '@angular/common';
import {catchError, map, tap} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router, private authService: AuthService, @Inject(PLATFORM_ID) private platformId: any) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | boolean {
    if (isPlatformBrowser(this.platformId)) {
      return this.authService.isAuthenticated().pipe(
        tap(isAuthenticated => {
          if (!isAuthenticated) {
            this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
          }
        }),
        catchError(() => {
          this.router.navigate(['/login']);
          return of(false);
        })
      );
    }

    // Se não estiver no navegador (SSR), redirecione para o login ou retorne false
    return false;
  }
}

