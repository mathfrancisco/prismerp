import { Component } from '@angular/core';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import {
  faUsers,
  faUserTie,
  faSitemap,
  faShoppingCart,
  faWarehouse,
  faFileInvoiceDollar
} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-services',
  standalone: true,
  imports: [FontAwesomeModule],
  templateUrl: './services.component.html',
  styleUrl: './services.component.scss'
})
export class ServicesComponent {
  constructor(library: FaIconLibrary) {
    library.addIcons(faUsers, faUserTie, faSitemap, faShoppingCart, faWarehouse, faFileInvoiceDollar);
  }
}
