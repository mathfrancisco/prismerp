import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { ProductService } from '../../../../core/services/product.service';
import { ProductDTO } from '../../../../core/models/product.model';
import { Router } from '@angular/router';
import { finalize } from 'rxjs/operators';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-product-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule], // Importe ReactiveFormsModule
  templateUrl: './product-create.component.html',
  styleUrl: './product-create.component.scss'
})
export class ProductCreateComponent {
  productForm!: FormGroup;
  isLoading = false;
  error: string | null = null;

  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    private router: Router
  ) {
    this.createForm();
  }

  createForm() {
    this.productForm = this.fb.group({
      code: ['', Validators.required],
      name: ['', Validators.required],
      description: [''],
      price: ['', [Validators.required, Validators.min(0)]],
      currentStock: ['', [Validators.required, Validators.min(0)]],
      minimumStock: ['', [Validators.required, Validators.min(0)]],
      category: ['', Validators.required],
      unit: [''],
      active: [true]
    });
  }


  createProduct() {
    if (this.productForm.invalid) {
      return;
    }

    this.isLoading = true;
    this.error = null; // Limpa qualquer erro anterior

    const newProduct: ProductDTO = this.productForm.value;

    this.productService.createProduct(newProduct).pipe(
      finalize(() => this.isLoading = false)
    ).subscribe({
      next: (product) => {
        // Produto criado com sucesso, redirecione para a lista de produtos
        this.router.navigate(['/products']);
      },
      error: (err) => {
        // Trate o erro
        this.error = `Error creating product: ${err.message}`;
        console.error('Erro ao criar produto:', err);
      }
    });
  }
}
