import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

export interface SalesOrderDTO {
  id: number;
  orderNumber: string;
  customerId: number;
  status: OrderStatus;
  totalAmount: number;
  orderDate: string;
  deliveryDate: string;
  items: SalesOrderItemDTO[];
}

export interface SalesOrderItemDTO {
  id: number;
  productId: number;
  quantity: number;
  unitPrice: number;
  totalPrice: number;
}

export enum OrderStatus {
  PENDING = 'PENDING',
  PROCESSING = 'PROCESSING',
  SHIPPED = 'SHIPPED',
  DELIVERED = 'DELIVERED',
  CANCELLED = 'CANCELLED'
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

@Injectable({
  providedIn: 'root'
})
export class SalesService {
  private readonly API_URL = '/api/v1/sales-orders';

  constructor(private http: HttpClient) {}

  createOrder(orderDTO: SalesOrderDTO): Observable<SalesOrderDTO> {
    return this.http.post<SalesOrderDTO>(this.API_URL, orderDTO);
  }

  getOrder(id: number): Observable<SalesOrderDTO> {
    return this.http.get<SalesOrderDTO>(`${this.API_URL}/${id}`);
  }

  updateStatus(id: number, status: OrderStatus): Observable<SalesOrderDTO> {
    return this.http.put<SalesOrderDTO>(
      `${this.API_URL}/${id}/status`,
      null,
      { params: new HttpParams().set('status', status) }
    );
  }

  getCustomerOrders(
    customerId: number,
    page: number = 0,
    size: number = 10
  ): Observable<Page<SalesOrderDTO>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<Page<SalesOrderDTO>>(
      `${this.API_URL}/customer/${customerId}`,
      { params }
    );
  }

  getByOrderNumber(orderNumber: string): Observable<SalesOrderDTO> {
    return this.http.get<SalesOrderDTO>(`${this.API_URL}/number/${orderNumber}`);
  }

  // Additional methods for chart data
  getSalesStats(): Observable<any> {
    return this.http.get<SalesOrderDTO[]>(`${this.API_URL}`).pipe(
      map(orders => this.processSalesData(orders))
    );
  }

  private processSalesData(orders: SalesOrderDTO[]) {
    // Process orders into chart-friendly format
    const monthlySales = new Map<string, number>();

    orders.forEach(order => {
      const month = new Date(order.orderDate).toLocaleString('default', { month: 'short' });
      monthlySales.set(
        month,
        (monthlySales.get(month) || 0) + order.totalAmount
      );
    });

    return {
      labels: Array.from(monthlySales.keys()),
      data: Array.from(monthlySales.values())
    };
  }
}
