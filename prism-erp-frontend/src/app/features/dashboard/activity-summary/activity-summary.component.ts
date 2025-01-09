import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterLink} from '@angular/router';

interface Activity {
  id: number;
  type: string;
  description: string;
  timestamp: string;
  status: 'completed' | 'pending' | 'in-progress';
  icon: string; // Propriedade para o ícone Font Awesome
}

@Component({
  selector: 'app-activity-summary',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './activity-summary.component.html',
  styleUrls: ['./activity-summary.component.scss']
})
export class ActivitySummaryComponent implements OnInit {
  activities: Activity[] = [
    {
      id: 1,
      type: 'sale',
      description: 'New sale order #1234 received',
      timestamp: '5 minutes ago',
      status: 'completed',
      icon: 'fa-cart-arrow-down'
    },
    {
      id: 2,
      type: 'inventory',
      description: 'Inventory update: 50 items added',
      timestamp: '1 hour ago',
      status: 'completed',
      icon: 'fa-boxes-stacked'
    },
    {
      id: 3,
      type: 'customer',
      description: 'New customer registration',
      timestamp: '2 hours ago',
      status: 'completed',
      icon: 'fa-user-plus'
    },
    {
      id: 4,
      type: 'task',
      description: 'Monthly report generation',
      timestamp: '3 hours ago',
      status: 'in-progress',
      icon: 'fa-file-lines'
    },
    {
      id: 5,
      type: 'alert',
      description: 'Low stock alert: Product XYZ',
      timestamp: '4 hours ago',
      status: 'pending',
      icon: 'fa-triangle-exclamation'
    }
  ];


  getIconClass(type: string): string {
    const baseClasses = 'flex-shrink-0 w-8 h-8 rounded-full flex items-center justify-center text-lg';
    switch (type) {
      case 'sale':
        return `${baseClasses} bg-emerald-100 text-emerald-600`;
      case 'inventory':
        return `${baseClasses} bg-blue-100 text-blue-600`;
      case 'customer':
        return `${baseClasses} bg-purple-100 text-purple-600`;
      case 'task':
        return `${baseClasses} bg-yellow-100 text-yellow-600`;
      case 'alert':
        return `${baseClasses} bg-red-100 text-red-600`;
      default:
        return `${baseClasses} bg-gray-100 text-gray-600`;
    }
  }


  getStatusClass(status: string): string {
    switch (status) {
      case 'completed':
        return 'bg-emerald-100 text-emerald-800';
      case 'in-progress':
        return 'bg-yellow-100 text-yellow-800';
      case 'pending':
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  }

  ngOnInit(): void {}
}

