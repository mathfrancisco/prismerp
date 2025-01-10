import { Component } from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule,FontAwesomeModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {
  sidebarSections = [
    {
      title: 'Main',
      items: [
        { label: 'Dashboard', path: '/dashboard', icon: 'fa-home' },
        { label: 'Activity', path: '/activity', icon: 'fa-chart-line' }
      ]
    },
    {
      title: 'Sales',
      items: [
        { label: 'Customers', path: '/sales/customers', icon: 'fa-users' },
        { label: 'Orders', path: '/sales/orders', icon: 'fa-shopping-cart' },
        { label: 'Quotations', path: '/sales/quotations', icon: 'fa-file-invoice' }
      ]
    },
    {
      title: 'Inventory',
      items: [
        { label: 'Products', path: '/inventory/products', icon: 'fa-box' },
        { label: 'Stock Levels', path: '/inventory/stock', icon: 'fa-warehouse' },
        { label: 'Transactions', path: '/inventory/transactions', icon: 'fa-exchange-alt' }
      ]
    },
    {
      title: 'Finance',
      items: [
        { label: 'Invoices', path: '/finance/invoices', icon: 'fa-file-invoice-dollar' },
        { label: 'Payments', path: '/finance/payments', icon: 'fa-credit-card' }
      ]
    }
  ];
}
