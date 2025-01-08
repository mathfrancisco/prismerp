import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Chart, ChartConfiguration } from 'chart.js/auto';
import { SalesService } from '../../../core/services/sales-order.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-sales-chart',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './sales-chart.component.html',
  styleUrl: './sales-chart.component.scss'
})
export class SalesChartComponent implements OnInit {
  @ViewChild('salesChart') private chartRef!: ElementRef;

  private chart!: Chart;
  public timePeriods = ['Week', 'Month', 'Year'];
  public selectedPeriod = 'Month';

  public totalSales = 0;
  public avgOrderValue = 0;
  public totalOrders = 0;

  constructor(private salesService: SalesService) {}

  ngOnInit(): void {
    this.initChart();
    this.loadSalesData();
  }

  private initChart(): void {
    const ctx = this.chartRef.nativeElement.getContext('2d');

    const config: ChartConfiguration = {
      type: 'line',
      data: {
        labels: [],
        datasets: [{
          label: 'Sales',
          data: [],
          borderColor: '#3B82F6',
          backgroundColor: 'rgba(59, 130, 246, 0.1)',
          fill: true,
          tension: 0.4
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            display: false
          }
        },
        scales: {
          y: {
            beginAtZero: true,
            grid: {
              display: true,
              drawOnChartArea: true,
              color: 'rgba(0, 0, 0, 0.1)',
              lineWidth: 1
            },
            ticks: {
              padding: 10
            }
          },
          x: {
            grid: {
              display: false,
              drawOnChartArea: false
            },
            ticks: {
              padding: 10
            }
          }
        }
      }
    };

    this.chart = new Chart(ctx, config);
  }

  updateChartPeriod(period: string): void {
    this.selectedPeriod = period;
    this.loadSalesData();
  }

  private loadSalesData(): void {
    this.salesService.getSalesStats().subscribe(data => {
      this.updateChartData(data.labels, data.data);

      // Update summary statistics
      this.totalSales = data.data.reduce((a: number, b: number) => a + b, 0);
      this.totalOrders = data.orders;
      this.avgOrderValue = this.totalSales / this.totalOrders;
    });
  }

  private updateChartData(labels: string[], data: number[]): void {
    this.chart.data.labels = labels;
    this.chart.data.datasets[0].data = data;
    this.chart.update();
  }
}
