// dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivitySummaryComponent } from './activity-summary/activity-summary.component';
import { ControlPanelComponent } from './control-panel/control-panel.component';
import { SalesChartComponent } from './sales-chart/sales-chart.component';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    ActivitySummaryComponent,
    ControlPanelComponent,
    SalesChartComponent
  ],
})
export class DashboardComponent implements OnInit {
  constructor() {}

  stats = [
    { label: 'Total Revenue', value: '$54,230.50', change: 12.5, icon: 'fa-dollar-sign', iconBg: 'bg-blue-100', iconColor: 'text-blue-600' },
    { label: 'Total Orders', value: '1,245', change: 8.2, icon: 'fa-shopping-cart', iconBg: 'bg-green-100', iconColor: 'text-green-600' },
    { label: 'Active Customers', value: '892', change: 5.3, icon: 'fa-users', iconBg: 'bg-purple-100', iconColor: 'text-purple-600' },
    { label: 'Task Completion', value: '94.2%', change: 2.1, icon: 'fa-check-circle', iconBg: 'bg-yellow-100', iconColor: 'text-yellow-600' }
  ];

  ngOnInit(): void {}
}
