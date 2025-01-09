// orders.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SalesService } from '../../../core/services/sales-order.service';
import { SalesOrderDTO, OrderStatus, Page } from '../../../core/models/sales-order.model';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms'; // Importe o FormsModule
import { MatInputModule } from '@angular/material/input'; // Importe o MatInputModule
import { MatFormFieldModule } from '@angular/material/form-field'; // Importe o MatFormFieldModule
import { MatSelectModule } from '@angular/material/select'; // Importe o MatSelectModule
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner'; // Importe para o spinner
import { Chart, registerables } from 'chart.js';
import {MatPaginator} from '@angular/material/paginator';


Chart.register(...registerables);

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [
    CommonModule,
    MatMenuModule,
    MatButtonModule,
    MatIconModule,
    FormsModule, // Adicione o FormsModule
    MatInputModule, // Adicione o MatInputModule
    MatFormFieldModule, // Adicione o MatFormFieldModule
    MatSelectModule, // Adicione o MatSelectModule
    MatProgressSpinnerModule,
    MatPaginator,
    // Adicione o MatProgressSpinnerModule
  ],
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.scss']
})
export class OrdersComponent implements OnInit {
  orders: SalesOrderDTO[] = [];
  orderStatuses = OrderStatus;
  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;
  loading = false;
  salesChart: Chart | null = null;
  orderNumberSearch: string = '';

  constructor(private salesService: SalesService) {}

  ngOnInit(): void {
    this.loadOrders();
    this.loadSalesStats();
  }

  loadOrders(page: number = this.currentPage): void {
    this.loading = true;
    this.salesService.getCustomerOrders(1, page, this.pageSize).subscribe({ //customerId: number,
      next: (response: Page<SalesOrderDTO>) => {
        this.orders = response.content;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        this.currentPage = response.number;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading orders:', error);
        this.loading = false;
      }
    });
  }

  loadSalesStats(): void {
    this.salesService.getSalesStats().subscribe({
      next: (data) => {
        if (this.salesChart) {
          this.salesChart.destroy();
        }
        this.salesChart = new Chart('salesChart', {
          type: 'bar',
          data: {
            labels: data.labels,
            datasets: [{
              label: 'Monthly Sales',
              data: data.data,
              backgroundColor: 'rgba(54, 162, 235, 0.2)',
              borderColor: 'rgba(54, 162, 235, 1)',
              borderWidth: 1
            }]
          },
          options: {
            scales: {
              y: {
                beginAtZero: true
              }
            }
          }
        });
      },
      error: (error) => {
        console.error('Error loading sales stats:', error);
      }
    });
  }

  updateOrderStatus(orderId: number, newStatus: string): void {
    this.loading = true;
    this.salesService.updateStatus(orderId, newStatus).subscribe({
      next: (updatedOrder) => {
        const index = this.orders.findIndex(o => o.id === updatedOrder.id);
        if (index !== -1) {
          this.orders[index] = updatedOrder;
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('Error updating order status:', error);
        this.loading = false;
      }
    });
  }

  onPageChange(page: number): void {
    this.loadOrders(page);
  }

  searchByOrderNumber(): void {
    if (this.orderNumberSearch) {
      this.loading = true;
      this.salesService.getByOrderNumber(this.orderNumberSearch).subscribe({
        next: (order) => {
          this.orders = [order]; // Exibe apenas o pedido encontrado
          this.totalElements = 1;
          this.totalPages = 1;
          this.currentPage = 0;
          this.loading = false;
        },
        error: (error) => {
          console.error('Error searching for order:', error);
          this.loading = false;
        }
      });
    } else {
      this.loadOrders(); // Volta para a lista completa se a busca estiver vazia
    }
  }


  protected readonly Object = Object;
}

