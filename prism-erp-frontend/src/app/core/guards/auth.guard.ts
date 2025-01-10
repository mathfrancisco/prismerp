import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import {CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree} from '@angular/router';
import { Observable, of } from 'rxjs';
import { AuthService } from '../auth/auth.service';
import { isPlatformBrowser } from '@angular/common';
import {catchError, map, tap} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router, private authService: AuthService, @Inject(PLATFORM_ID) private platformId: any) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (isPlatformBrowser(this.platformId)) {
      return this.authService.isAuthenticated().pipe(
        map((isAuthenticated) => {
          if (isAuthenticated) {
            return true;
          } else {
            this.router.navigate(['/login'], {
              queryParams: { returnUrl: state.url },
            });
            return false;
          }
        })
      );
    }
    return false; // Ou redirecione para /login se não estiver no navegador
  }

}

