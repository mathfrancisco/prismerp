export class Product {
}

export interface ProductDTO {
  id?: number;
  code: string;
  name: string;
  description?: string;
  price: number; // Ou string, dependendo de como você lida com preços no frontend
  currentStock: number;
  minimumStock: number;
  category: string;
  unit?: string;
  active: boolean;
}

