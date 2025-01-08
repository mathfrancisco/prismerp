// product-edit.component.ts
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from '../../../../core/services/product.service';
import { ProductDTO } from '../../../../core/models/product.model';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { finalize, switchMap } from 'rxjs/operators';
import {CommonModule, Location, NgIf} from '@angular/common';


@Component({
  selector: 'app-product-edit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ReactiveFormsModule, NgIf],
  templateUrl: './product-edit.component.html',
  styleUrl: './product-edit.component.scss'
})
export class ProductEditComponent implements OnInit {
  productForm!: FormGroup;
  product: ProductDTO | undefined;
  isLoading = false;
  error: string | null = null;
  productId!: number;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private productService: ProductService,
    private fb: FormBuilder,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.getProduct();
  }

  getProduct(): void {
    this.isLoading = true;
    this.route.paramMap.pipe(
      switchMap(params => {
        this.productId = Number(params.get('id'));
        return this.productService.getProductById(this.productId);
      }),
      finalize(() => this.isLoading = false)
    ).subscribe({
      next: (product) => {
        this.product = product;
        this.createForm();
      },
      error: (error) => {
        this.error = `Error loading product: ${error.message}`;
        console.error('Error loading product:', error);
      }
    });
  }

  createForm(): void {
    this.productForm = this.fb.group({
      code: [this.product?.code, Validators.required],
      name: [this.product?.name, Validators.required],
      description: [this.product?.description],
      price: [this.product?.price, [Validators.required, Validators.min(0)]],
      currentStock: [this.product?.currentStock, [Validators.required, Validators.min(0)]],
      minimumStock: [this.product?.minimumStock, [Validators.required, Validators.min(0)]],
      category: [this.product?.category, Validators.required],
      unit: [this.product?.unit],
      active: [this.product?.active]
    });
  }

  updateProduct() {
    if (this.productForm.invalid || this.isLoading) {
      return;
    }

    this.isLoading = true;
    this.error = null;

    const updatedProduct: ProductDTO = { ...this.product, ...this.productForm.value };

    this.productService.updateProduct(this.productId, updatedProduct).pipe(
      finalize(() => this.isLoading = false)
    ).subscribe({
      next: () => {
        this.router.navigate(['/products']);
      },
      error: (error) => {
        this.error = `Error updating product: ${error.message}`;
        console.error('Error updating product:', error);
      }
    });
  }

  goBack(): void {
    this.location.back();
  }
}

