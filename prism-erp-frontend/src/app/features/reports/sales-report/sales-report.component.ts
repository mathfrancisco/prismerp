// sales-report.component.ts
import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Chart } from 'chart.js/auto';
import { ReportBaseComponent } from '../report-base/report-base.component';

@Component({
  selector: 'app-sales-report',
  standalone: true,
  imports: [CommonModule, ReportBaseComponent],
  templateUrl: './sales-report.component.html',
  styleUrl: './sales-report.component.scss'
})
export class SalesReportComponent implements OnInit {
  @ViewChild('salesTrendChart') private salesTrendChartRef!: ElementRef;
  @ViewChild('productPerformanceChart') private productPerformanceChartRef!: ElementRef;

  topProducts = [
    { name: 'Product X', sales: 1250, growth: 15.5 },
    { name: 'Product Y', sales: 980, growth: 8.3 },
    { name: 'Product Z', sales: 850, growth: -2.1 }
  ];

  ngOnInit(): void {
    this.initCharts();
  }

  private initCharts(): void {
    // Sales Trend Chart
    new Chart(this.salesTrendChartRef.nativeElement, {
      type: 'line',
      data: {
        labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
        datasets: [{
          label: 'Sales Volume',
          data: [45000, 52000, 49000, 58000, 55000, 62000],
          borderColor: '#3B82F6',
          tension: 0.4
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false
      }
    });

    // Product Performance Chart
    new Chart(this.productPerformanceChartRef.nativeElement, {
      type: 'bar',
      data: {
        labels: ['Product X', 'Product Y', 'Product Z', 'Product W', 'Product V'],
        datasets: [{
          label: 'Sales by Product',
          data: [1250, 980, 850, 720, 650],
          backgroundColor: '#60A5FA'
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        indexAxis: 'y'
      }
    });
  }
}
