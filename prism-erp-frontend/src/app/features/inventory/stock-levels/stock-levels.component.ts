// stock-levels.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { InventoryService } from '../../../core/services/inventory.service';
import { ProductStockDTO } from '../../../core/models/inventory-transaction.model';
import { Page } from '../../../core/models/user.model';

@Component({
  selector: 'app-stock-levels',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './stock-levels.component.html',
  styleUrls: ['./stock-levels.component.scss']
})
export class StockLevelsComponent implements OnInit {
  products: ProductStockDTO[] = [];
  lowStockProducts: ProductStockDTO[] = [];
  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;
  loading = false;
  error: string | null = null;
  showLowStockOnly = false;

  constructor(private inventoryService: InventoryService) {}

  ngOnInit(): void {
    this.loadStockLevels();
    this.loadLowStockProducts();
  }

  loadStockLevels(page: number = this.currentPage): void {
    this.loading = true;
    this.inventoryService.getStockLevels(page, this.pageSize)
      .subscribe({
        next: (response: Page<ProductStockDTO>) => {
          this.products = response.content;
          this.totalElements = response.totalElements;
          this.totalPages = response.totalPages;
          this.currentPage = response.number;
          this.loading = false;
        },
        error: (error) => {
          this.error = 'Failed to load stock levels';
          this.loading = false;
          console.error('Error loading stock levels:', error);
        }
      });
  }

  loadLowStockProducts(): void {
    this.inventoryService.getLowStockProducts()
      .subscribe({
        next: (products) => {
          this.lowStockProducts = products;
        },
        error: (error) => {
          console.error('Error loading low stock products:', error);
        }
      });
  }

  onPageChange(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadStockLevels(page);
    }
  }

  toggleLowStockView(): void {
    this.showLowStockOnly = !this.showLowStockOnly;
    if (this.showLowStockOnly) {
      this.products = this.lowStockProducts;
    } else {
      this.loadStockLevels();
    }
  }

  getStockStatusClass(product: ProductStockDTO): string {
    if (product.lowStock) {
      return 'text-red-600 bg-red-100';
    }
    return product.currentStock > product.minimumStock * 2
      ? 'text-green-600 bg-green-100'
      : 'text-yellow-600 bg-yellow-100';
  }
}
