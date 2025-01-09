import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import {
  faRocket,
  faComments,
  faUsers,
  faUserTie,
  faShoppingCart,
  faWarehouse,
  faFileInvoiceDollar,
  faChartLine,
  faChevronLeft,
  faChevronRight,
  faCheck,
  faArrowRight,
  faSitemap,
  faLock,
  faMobileAlt,
  faSyncAlt,
  faHeadset
} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    FontAwesomeModule
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'prism-erp-frontend';
  constructor(library: FaIconLibrary) {
    // Add icons to the library for convenient access in components
    library.addIcons(
      faRocket,
      faComments,
      faUsers,
      faUserTie,
      faShoppingCart,
      faWarehouse,
      faFileInvoiceDollar,
      faChartLine,
      faChevronLeft,
      faChevronRight,
      faCheck,
      faArrowRight,
      faSitemap,
      faLock,
      faMobileAlt,
      faSyncAlt,
      faHeadset
    );
  }
}
