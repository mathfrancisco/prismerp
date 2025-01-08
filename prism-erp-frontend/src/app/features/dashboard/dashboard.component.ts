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

  ngOnInit(): void {}
}
