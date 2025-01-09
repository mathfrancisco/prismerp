export interface InventoryTransactionDTO {
  id: number;
  productId: number;
  productName: string;
  type: 'INBOUND' | 'OUTBOUND'; // Defina o tipo explicitamente
  quantity: number;
  reference: string;
  notes: string;
}

export interface ProductStockDTO {
  productId: number;
  productName: string;
  productCode: string;
  currentStock: number;
  minimumStock: number;
  category: string;
  lowStock: boolean;
}
