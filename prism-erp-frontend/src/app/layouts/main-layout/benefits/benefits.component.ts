import { Component } from '@angular/core';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import {
  faRocket,
  faChartLine,
  faLock,
  faMobileAlt,
  faSyncAlt,
  faHeadset
} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-benefits',
  standalone: true,
  imports: [FontAwesomeModule],
  templateUrl: './benefits.component.html',
  styleUrl: './benefits.component.scss'
})
export class BenefitsComponent {
  constructor(library: FaIconLibrary) {
    library.addIcons(faRocket, faChartLine, faLock, faMobileAlt, faSyncAlt, faHeadset);
  }
}
