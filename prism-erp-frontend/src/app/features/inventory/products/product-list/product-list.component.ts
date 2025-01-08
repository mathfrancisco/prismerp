// product-list.component.ts
import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../../../core/services/product.service';
import { ProductDTO } from '../../../../core/models/product.model';
import { finalize } from 'rxjs/operators';
import {CommonModule, NgForOf, NgIf} from '@angular/common';
import {RouterLink, RouterModule} from '@angular/router';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, RouterModule, NgIf, NgForOf, RouterLink], // Importe RouterModule
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.scss'
})
export class ProductListComponent implements OnInit {
  products: ProductDTO[] = [];
  isLoading = false;
  error: string | null = null;

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.isLoading = true;
    this.productService.getProducts().pipe(
      finalize(() => this.isLoading = false)
    ).subscribe({
      next: (products) => {
        this.products = products;
      },
      error: (error) => {
        this.error = 'Error loading products.';
        console.error('Error loading products:', error);
      }
    });
  }


  deleteProduct(product: ProductDTO): void {
    if (confirm(`Are you sure you want to delete ${product.name}?`)) {
      this.productService.deleteProduct(product.id!).subscribe({
        next: () => this.loadProducts(),
        error: (error) => {
          console.error('Error deleting product:', error);
          // Handle error appropriately, e.g., display an error message
        }
      });
    }
  }
}

