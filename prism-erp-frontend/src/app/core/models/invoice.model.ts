export interface InvoiceDTO {
  id: number;
  invoiceNumber: string;
  salesOrderId: number;
  totalAmount: number;
  status: InvoiceStatus;
  invoiceDate: string; // Use string para datas (yyyy-MM-dd)
  dueDate: string;     // Use string para datas (yyyy-MM-dd)
}

export interface InvoiceTaxCalculationDTO {
  subtotal: number;
  discountAmount: number;
  taxAmount: number;
  totalAmount: number;
  icmsAmount: number;
  pisAmount: number;
  cofinsAmount: number;
  issAmount: number;
}

// invoice-status.enum.ts (Crie este arquivo se ainda não existir)
export enum InvoiceStatus {
  DRAFT = 'DRAFT',
  ISSUED = 'ISSUED',
  PAID = 'PAID',
  PARTIALLY_PAID = 'PARTIALLY_PAID',
  OVERDUE = 'OVERDUE',
  CANCELLED = 'CANCELLED',
  VOID = 'VOID'
}
