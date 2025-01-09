import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SalesService } from '../../../core/services/sales-order.service';
import { SalesOrderDTO, OrderStatus, Page } from '../../../core/models/sales-order.model';
import { SalesOrderItemDTO } from '../../../core/models/sales-order-item.model';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { Chart, registerables } from 'chart.js';
import { MatPaginator } from '@angular/material/paginator';
import { OrdersDetailsComponent } from '../orders-details/orders-details.component';

Chart.register(...registerables);

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [
    CommonModule,
    MatMenuModule,
    MatButtonModule,
    MatIconModule,
    FormsModule,
    ReactiveFormsModule,
    MatInputModule,
    MatFormFieldModule,
    MatSelectModule,
    MatProgressSpinnerModule,
    MatPaginator,
    MatDialogModule
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
  orderNumberSearch = '';
  selectedOrder: SalesOrderDTO | null = null;
  orderForm: FormGroup;

  constructor(
    private salesService: SalesService,
    private fb: FormBuilder,
    private dialog: MatDialog
  ) {
    this.orderForm = this.fb.group({
      customerId: ['', Validators.required],
      orderDate: ['', Validators.required],
      deliveryDate: [''],
      totalAmount: ['', [Validators.required, Validators.min(0)]],
      status: [OrderStatus.PENDING],
      items: this.fb.array([])
    });
  }

  ngOnInit(): void {
    this.loadOrders();
    this.loadSalesStats();
  }

  loadOrders(page: number = this.currentPage): void {
    this.loading = true;
    this.salesService.getCustomerOrders(1, page, this.pageSize).subscribe({
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
          this.orders = [order];
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
      this.loadOrders();
    }
  }

  createOrder(): void {
    if (this.orderForm.valid) {
      this.loading = true;
      const orderData = this.orderForm.value;

      this.salesService.createOrder(orderData).subscribe({
        next: (newOrder) => {
          this.orders.unshift(newOrder);
          this.orderForm.reset();
          this.loading = false;
        },
        error: (error) => {
          console.error('Error creating order:', error);
          this.loading = false;
        }
      });
    }
  }

  getOrder(id: number): void {
    this.loading = true;
    this.salesService.getOrder(id).subscribe({
      next: (order) => {
        this.selectedOrder = order;
        this.loading = false;
        this.openOrderDetails(order);
      },
      error: (error) => {
        console.error('Error fetching order:', error);
        this.loading = false;
      }
    });
  }

  openOrderDetails(order: SalesOrderDTO): void {
    const dialogRef = this.dialog.open(OrdersDetailsComponent, {
      width: '600px',
      data: order
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadOrders();
      }
    });
  }

  addOrderItem(): void {
    const items = this.orderForm.get('items') as FormArray;
    items.push(this.createOrderItemFormGroup());
  }

  removeOrderItem(index: number): void {
    const items = this.orderForm.get('items') as FormArray;
    items.removeAt(index);
  }

  private createOrderItemFormGroup(): FormGroup {
    return this.fb.group({
      productId: ['', Validators.required],
      quantity: [1, [Validators.required, Validators.min(1)]],
      price: ['', [Validators.required, Validators.min(0)]]
    });
  }

  get itemsFormArray() {
    return this.orderForm.get('items') as FormArray;
  }
  protected readonly Object = Object;
}
