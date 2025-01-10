import { enableProdMode, importProvidersFrom } from '@angular/core';
import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { environment } from '../environment/environment';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { provideHttpClient } from '@angular/common/http';
import { ApplicationConfig } from '@angular/core';

if (environment.production) {
  enableProdMode();
}

bootstrapApplication(AppComponent,  {
  providers: [
    provideRouter(routes),
    provideHttpClient()
  ]
})
  .catch(err => console.error(err));
