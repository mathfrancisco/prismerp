import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterModule} from '@angular/router';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import {HomeComponent} from './home/home.component';
import {ServicesComponent} from './services/services.component';
import {BenefitsComponent} from './benefits/benefits.component';
import {AboutComponent} from './about/about.component';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [
    HeaderComponent,
    FooterComponent,
    RouterModule,
    CommonModule,
    HomeComponent,
    ServicesComponent,
    BenefitsComponent,
    AboutComponent
  ],
  templateUrl: './main-layout.component.html',
  styleUrl: './main-layout.component.scss'
})
export class MainLayoutComponent {

}
