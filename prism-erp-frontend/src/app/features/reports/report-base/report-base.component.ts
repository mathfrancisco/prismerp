import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Chart } from 'chart.js/auto';

@Component({
  selector: 'app-report-base',
  standalone: true,
  imports: [CommonModule,Chart],
  templateUrl: './report-base.component.html',
  styleUrl: './report-base.component.scss'
})
export class ReportBaseComponent {
  @Input() title: string = '';

  onPeriodChange(event: any): void {
    console.log('Period changed:', event.target.value);
  }

  exportReport(): void {
    console.log('Exporting report...');
  }
}
