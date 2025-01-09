import {SalesOrderItemDTO} from './sales-order-item.model';

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
