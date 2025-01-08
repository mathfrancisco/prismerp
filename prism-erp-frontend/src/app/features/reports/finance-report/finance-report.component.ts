import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Chart } from 'chart.js/auto';
import { ReportBaseComponent } from '../report-base/report-base.component';

@Component({
  selector: 'app-finance-report',
  standalone: true,
  imports: [CommonModule, ReportBaseComponent],
  templateUrl: './finance-report.component.html',
  styleUrl: './finance-report.component.scss'
})
export class FinanceReportComponent implements OnInit {
  @ViewChild('revenueChart') private revenueChartRef!: ElementRef;
  @ViewChild('expensesChart') private expensesChartRef!: ElementRef;

  ngOnInit(): void {
    this.initCharts();
  }

  private initCharts(): void {
    // Revenue Chart
    new Chart(this.revenueChartRef.nativeElement, {
      type: 'line',
      data: {
        labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
        datasets: [{
          label: 'Revenue',
          data: [30000, 35000, 32000, 40000, 38000, 45000],
          borderColor: '#10B981',
          tension: 0.4
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false
      }
    });

    // Expenses Chart
    new Chart(this.expensesChartRef.nativeElement, {
      type: 'bar',
      data: {
        labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
        datasets: [{
          label: 'Expenses',
          data: [25000, 28000, 24000, 32000, 30000, 35000],
          backgroundColor: '#8B5CF6'
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false
      }
    });
  }
}
