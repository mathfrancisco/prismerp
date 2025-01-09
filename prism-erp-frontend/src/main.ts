import { enableProdMode, importProvidersFrom } from '@angular/core';
import { bootstrapApplication } from '@angular/platform-browser';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideAnimations } from '@angular/platform-browser/animations';
import { AppComponent } from './app/app.component';
import { routes } from './app/app.routes';
import { environment } from './environment/environment';
import { AuthInterceptor } from './app/core/interceptors/authinterceptor';

if (environment.production) {
  enableProdMode();
}

const appConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptors([AuthInterceptor])),
    provideAnimations(),
  ]
};

bootstrapApplication(AppComponent, appConfig)
  .catch(err => console.error('Error bootstrapping application:', err));
