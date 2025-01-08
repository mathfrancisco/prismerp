// product-detail.component.ts
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../../../core/services/product.service';
import { ProductDTO } from '../../../../core/models/product.model';
import {CommonModule, Location} from '@angular/common';
import { finalize, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.scss'
})
export class ProductDetailComponent implements OnInit {
  product: ProductDTO | undefined;
  isLoading = false;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.getProduct();
  }

  getProduct(): void {
    this.isLoading = true;
    this.route.paramMap.pipe(
      switchMap(params => {
        const id = Number(params.get('id'));
        return this.productService.getProductById(id);
      }),
      finalize(() => this.isLoading = false)
    ).subscribe({
      next: (product) => {
        this.product = product;
      },
      error: (error) => {
        this.error = `Error loading product: ${error.message}`;
        console.error('Error loading product:', error);
      }
    });
  }

  goBack(): void {
    this.location.back();
  }
}
