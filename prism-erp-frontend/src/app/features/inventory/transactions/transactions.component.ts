// transactions.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { InventoryService } from '../../../core/services/inventory.service';
import { InventoryTransactionDTO } from '../../../core/models/inventory-transaction.model';

@Component({
  selector: 'app-transactions',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './transactions.component.html',
  styleUrls: ['./transactions.component.scss']
})
export class TransactionsComponent implements OnInit {
  transactionForm: FormGroup;
  transactions: InventoryTransactionDTO[] = [];
  loading = false;
  error: string | null = null;
  success: string | null = null;

  constructor(
    private fb: FormBuilder,
    private inventoryService: InventoryService
  ) {
    this.transactionForm = this.fb.group({
      productId: ['', Validators.required],
      type: ['INBOUND', Validators.required],
      quantity: ['', [Validators.required, Validators.min(1)]],
      reference: ['', Validators.required],
      notes: ['']
    });
  }

  ngOnInit(): void {
    // If you have a specific product ID, you can load its transactions here
  }

  submitTransaction(): void {
    if (this.transactionForm.valid) {
      this.loading = true;
      this.error = null;
      this.success = null;

      const transaction: InventoryTransactionDTO = {
        ...this.transactionForm.value,
        id: 0,
        productName: '' // This will be filled by the backend
      };

      this.inventoryService.createTransaction(transaction)
        .subscribe({
          next: (response) => {
            this.success = 'Transaction created successfully';
            this.loading = false;
            this.transactionForm.reset({
              type: 'INBOUND'
            });
            // Optionally reload transactions for the product
            if (transaction.productId) {
              this.loadTransactionsForProduct(transaction.productId);
            }
          },
          error: (error) => {
            this.error = 'Failed to create transaction';
            this.loading = false;
            console.error('Error creating transaction:', error);
          }
        });
    }
  }

  loadTransactionsForProduct(productId: number): void {
    this.loading = true;
    this.inventoryService.getTransactionsByProductId(productId)
      .subscribe({
        next: (transactions) => {
          this.transactions = transactions;
          this.loading = false;
        },
        error: (error) => {
          this.error = 'Failed to load transactions';
          this.loading = false;
          console.error('Error loading transactions:', error);
        }
      });
  }

  getTransactionTypeClass(type: 'INBOUND' | 'OUTBOUND'): string {
    return type === 'INBOUND'
      ? 'text-green-600 bg-green-100'
      : 'text-red-600 bg-red-100';
  }
}
