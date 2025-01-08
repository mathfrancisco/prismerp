import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
  navItems = [
    { label: 'Dashboard', path: '/dashboard' },
    { label: 'Sales', path: '/sales' },
    { label: 'Inventory', path: '/inventory' },
    { label: 'Finance', path: '/finance' },
    { label: 'Reports', path: '/reports' }
  ];
}
