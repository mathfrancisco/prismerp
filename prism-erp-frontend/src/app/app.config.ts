import { ApplicationConfig } from '@angular/core';
import {provideHttpClient, withFetch, withInterceptors} from '@angular/common/http';

import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import {AuthInterceptor} from './core/interceptors/authinterceptor';
import {provideAnimations} from '@angular/platform-browser/animations';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptors([AuthInterceptor]), withFetch()),
    provideAnimations()
    // Outros provedores
  ],
};
