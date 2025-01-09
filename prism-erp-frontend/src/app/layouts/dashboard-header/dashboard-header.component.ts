import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import {AuthService} from '../../core/auth/auth.service';

@Component({
  selector: 'app-dashboard-header',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard-header.component.html',
  styleUrls: ['./dashboard-header.component.scss']
})
export class DashboardHeaderComponent {

  constructor(private authService: AuthService) { }
  
  menuItems = [
    { path: '/dashboard', label: 'Dashboard' },
    { path: '/sales', label: 'Vendas' },
    { path: '/inventory', label: 'Estoque' },
    { path: '/finance', label: 'Financeiro' }

  ];

  logout(): void {
    this.authService.logout();
  }
}
