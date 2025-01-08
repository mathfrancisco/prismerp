import { Component } from '@angular/core';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import {
  faRocket,
  faInfoCircle,
  faChevronLeft,
  faChevronRight
} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [FontAwesomeModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  currentIndex = 0;
  items = [1, 2, 3, 4, 5, 6];
  constructor(library: FaIconLibrary) {
    library.addIcons(faRocket, faInfoCircle, faChevronLeft, faChevronRight);
  }

  prevSlide() {
    this.currentIndex = (this.currentIndex - 1 + this.items.length) % this.items.length;
  }

  nextSlide() {
    this.currentIndex = (this.currentIndex + 1) % this.items.length;
  }
}
