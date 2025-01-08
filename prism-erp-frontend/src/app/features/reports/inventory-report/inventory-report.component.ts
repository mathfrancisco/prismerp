import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Chart } from 'chart.js/auto';
import {ReportBaseComponent} from '../report-base/report-base.component';


@Component({
  selector: 'app-inventory-report',
  standalone: true,
  imports: [
    ReportBaseComponent,
    CommonModule
  ],
  templateUrl: './inventory-report.component.html',
  styleUrl: './inventory-report.component.scss'
})
export class InventoryReportComponent implements OnInit {
  @ViewChild('stockLevelChart') private stockLevelChartRef!: ElementRef;
  @ViewChild('turnoverChart') private turnoverChartRef!: ElementRef;

  lowStockItems = [
    { name: 'Product A', currentStock: 15, minLevel: 20, urgent: true },
    { name: 'Product B', currentStock: 25, minLevel: 30, urgent: false },
    { name: 'Product C', currentStock: 10, minLevel: 25, urgent: true }
  ];

  ngOnInit(): void {
    this.initCharts();
  }

  private initCharts(): void {
    // Stock Level Chart
    new Chart(this.stockLevelChartRef.nativeElement, {
      type: 'bar',
      data: {
        labels: ['Category A', 'Category B', 'Category C', 'Category D'],
        datasets: [{
          label: 'Current Stock Level',
          data: [150, 240, 180, 320],
          backgroundColor: '#F59E0B'
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false
      }
    });

    // Turnover Chart
    new Chart(this.turnoverChartRef.nativeElement, {
      type: 'line',
      data: {
        labels: ['Week 1', 'Week 2', 'Week 3', 'Week 4'],
        datasets: [{
          label: 'Stock Turnover Rate',
          data: [3.2, 4.1, 3.8, 4.5],
          borderColor: '#10B981',
          tension: 0.4
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false
      }
    });
  }
}
