import { Component } from '@angular/core';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faCube, faBars, faArrowRight } from '@fortawesome/free-solid-svg-icons';
import { RouterModule } from '@angular/router'; // Importe RouterModule

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [FontAwesomeModule, RouterModule], // Adicione RouterModule aos imports
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
  constructor(library: FaIconLibrary) {
    library.addIcons(faCube, faBars, faArrowRight);
  }
}
