import { Component, Inject } from '@angular/core';
import {MatDialogRef, MAT_DIALOG_DATA, MatDialogModule} from '@angular/material/dialog';
import { SalesOrderDTO } from '../../../core/models/sales-order.model';
import {CommonModule} from '@angular/common';
import {MatButtonModule} from '@angular/material/button';

@Component({
  selector: 'app-order-details',
  template: `
    <h2 mat-dialog-title>Order Details - {{data.orderNumber}}</h2>
    <mat-dialog-content>
      <div class="grid grid-cols-2 gap-4 p-4">
        <div>
          <strong>Customer ID:</strong> {{data.customerId}}
        </div>
        <div>
          <strong>Order Date:</strong> {{data.orderDate | date}}
        </div>
        <div>
          <strong>Delivery Date:</strong> {{data.deliveryDate | date}}
        </div>
        <div>
          <strong>Status:</strong> {{data.status}}
        </div>
        <div>
          <strong>Total Amount:</strong> {{data.totalAmount | currency}}
        </div>
      </div>

      <div class="mt-4">
        <h3 class="font-bold mb-2">Order Items</h3>
        <table class="min-w-full divide-y divide-gray-200">
          <thead>
            <tr>
              <th>Product ID</th>
              <th>Quantity</th>
              <th>Price</th>
              <th>Total</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let item of data.items">
              <td>{{item.productId}}</td>
              <td>{{item.quantity}}</td>
              <td>{{item.price | currency}}</td>
              <td>{{item.quantity * item.price | currency}}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button mat-dialog-close>Close</button>
    </mat-dialog-actions>
  `,
  standalone: true,
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule
  ]
})
export class OrdersDetailsComponent {
  constructor(
    public dialogRef: MatDialogRef<OrdersDetailsComponent>,
    @Inject(MAT_DIALOG_DATA) public data: SalesOrderDTO
  ) {}
}
