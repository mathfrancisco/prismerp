import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

// Guards
import { AuthGuard } from './core/guards/auth.guard';

// Components
import { DashboardComponent } from './features/dashboard/dashboard.component';

// Auth Components
import { LoginComponent } from './core/auth/login/login.component';
import { RegisterComponent } from './core/auth/register/register.component';
import { ForgotPasswordComponent } from './core/auth/forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './core/auth/reset-password/reset-password.component';

// User Management Components
import { UserListComponent } from './features/users/user-list/user-list.component';
import { UserDetailComponent } from './features/users/user-detail/user-detail.component';

// Finance Components
import { InvoicesComponent } from './features/finance/invoices/invoices.component';
import { PaymentsComponent } from './features/finance/payments/payments.component';
import { ReportsComponent as FinanceReportsComponent } from './features/finance/reports/reports.component';

// HR Components
import { EmployeesComponent } from './features/hr/employees/employees.component';
import { DepartmentsComponent } from './features/hr/departments/departments.component';

// Inventory Components
import { ProductsComponent } from './features/inventory/products/products.component';
import { StockLevelsComponent } from './features/inventory/stock-levels/stock-levels.component';
import { TransactionsComponent } from './features/inventory/transactions/transactions.component';

// Purchase Components
import { OrdersComponent as PurchaseOrdersComponent } from './features/purchase/orders/orders.component';
import { SuppliersComponent } from './features/purchase/suppliers/suppliers.component';

// Reports Components
import { SalesReportComponent } from './features/reports/sales-report/sales-report.component';
import { InventoryReportComponent } from './features/reports/inventory-report/inventory-report.component';
import { FinanceReportComponent } from './features/reports/finance-report/finance-report.component';

// Sales Components
import { OrdersComponent as SalesOrdersComponent } from './features/sales/orders/orders.component';
import { CustomersComponent } from './features/sales/customers/customers.component';
import { QuotationsComponent } from './features/sales/quotations/quotations.component';

export const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },

  // Public Routes
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'forgot-password', component: ForgotPasswordComponent },
  { path: 'reset-password', component: ResetPasswordComponent },

  // Protected Routes
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },

  // User Management
  {
    path: 'users',
    canActivate: [AuthGuard],
    children: [
      { path: '', component: UserListComponent },
      { path: ':id', component: UserDetailComponent },
    ],
  },

  // Finance Module
  {
    path: 'finance',
    canActivate: [AuthGuard],
    children: [
      { path: 'invoices', component: InvoicesComponent },
      { path: 'payments', component: PaymentsComponent },
      { path: 'reports', component: FinanceReportsComponent },
    ],
  },

  // HR Module
  {
    path: 'hr',
    canActivate: [AuthGuard],
    children: [
      { path: 'employees', component: EmployeesComponent },
      { path: 'departments', component: DepartmentsComponent },
    ],
  },

  // Inventory Module
  {
    path: 'inventory',
    canActivate: [AuthGuard],
    children: [
      { path: 'products', component: ProductsComponent },
      { path: 'stock-levels', component: StockLevelsComponent },
      { path: 'transactions', component: TransactionsComponent },
    ],
  },

  // Purchase Module
  {
    path: 'purchase',
    canActivate: [AuthGuard],
    children: [
      { path: 'orders', component: PurchaseOrdersComponent },
      { path: 'suppliers', component: SuppliersComponent },
    ],
  },

  // Reports Module
  {
    path: 'reports',
    canActivate: [AuthGuard],
    children: [
      { path: 'sales', component: SalesReportComponent },
      { path: 'inventory', component: InventoryReportComponent },
      { path: 'finance', component: FinanceReportComponent },
    ],
  },

  // Sales Module
  {
    path: 'sales',
    canActivate: [AuthGuard],
    children: [
      { path: 'orders', component: SalesOrdersComponent },
      { path: 'customers', component: CustomersComponent },
      { path: 'quotations', component: QuotationsComponent },
    ],
  },

  // Wildcard Route for 404 - Not Found
  { path: '**', redirectTo: '/dashboard' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
