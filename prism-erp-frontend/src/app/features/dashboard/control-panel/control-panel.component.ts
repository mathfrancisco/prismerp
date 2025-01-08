import { Component, OnInit } from '@angular/core';
import {CommonModule, NgForOf} from '@angular/common';
import {RouterLink, RouterModule} from '@angular/router';

interface QuickAction {
  id: number;
  title: string;
  icon: string;
  color: string;
  route: string;
}

interface SystemStatus {
  name: string;
  status: 'operational' | 'warning' | 'error';
  details?: string;
}

@Component({
  selector: 'app-control-panel',
  standalone: true,
  imports: [
    RouterLink,
    NgForOf,
    CommonModule,
    RouterModule
  ],
  templateUrl: './control-panel.component.html',
  styleUrl: './control-panel.component.scss'
})
export class ControlPanelComponent implements OnInit {
  quickActions: QuickAction[] = [
    {
      id: 1,
      title: 'New Sale',
      icon: 'bi bi-cart-plus',
      color: 'bg-blue-100 text-blue-600',
      route: '/sales/new'
    },
    {
      id: 2,
      title: 'Add Product',
      icon: 'bi bi-box-seam',
      color: 'bg-green-100 text-green-600',
      route: '/products/new'
    },
    {
      id: 3,
      title: 'New Customer',
      icon: 'bi bi-person-plus',
      color: 'bg-purple-100 text-purple-600',
      route: '/customers/new'
    },
    {
      id: 4,
      title: 'Reports',
      icon: 'bi bi-file-earmark-text',
      color: 'bg-yellow-100 text-yellow-600',
      route: '/reports'
    }
  ];

  systemStatus: SystemStatus[] = [
    {
      name: 'Server Status',
      status: 'operational',
      details: 'Last checked: 2 minutes ago'
    },
    {
      name: 'Database',
      status: 'operational',
      details: 'Response time: 45ms'
    },
    {
      name: 'API Status',
      status: 'warning',
      details: 'High latency detected'
    }
  ];

  resourceUsage = [
    { name: 'CPU Usage', value: 45 },
    { name: 'Memory Usage', value: 72 },
    { name: 'Storage', value: 28 }
  ];

  constructor() {}

  ngOnInit(): void {
    this.startStatusPolling();
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'operational':
        return 'bg-green-100 text-green-800';
      case 'warning':
        return 'bg-yellow-100 text-yellow-800';
      case 'error':
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  }

  getResourceClass(value: number): string {
    if (value >= 90) return 'bg-red-500';
    if (value >= 75) return 'bg-yellow-500';
    return 'bg-green-500';
  }

  refreshStatus(): void {
    // Implement status refresh logic here
    console.log('Refreshing system status...');
  }

  private startStatusPolling(): void {
    // Poll for status updates every 5 minutes
    setInterval(() => {
      this.refreshStatus();
    }, 300000);
  }
}
