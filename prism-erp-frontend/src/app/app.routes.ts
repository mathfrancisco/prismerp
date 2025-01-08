import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

// Guards
import { AuthGuard } from './core/guards/auth.guard';

// Layouts
import { MainLayoutComponent } from './layouts/main-layout/main-layout.component';

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

// Dashboard Component
import { DashboardComponent } from './features/dashboard/dashboard.component';
import {CustomerDetailComponent} from './features/sales/customers/customer-detail/customer-detail.component';
import {CustomerListComponent} from './features/sales/customers/customer-list/customer-list.component';
import {ProductDetailComponent} from './features/inventory/products/product-detail/product-detail.component';
import {ProductEditComponent} from './features/inventory/products/product-edit/product-edit.component';
import {ProductCreateComponent} from './features/inventory/products/product-create/product-create.component';
import {ProductListComponent} from './features/inventory/products/product-list/product-list.component';
import {UserCreateComponent} from './features/users/user-create/user-create.component';
import {CustomerFormComponent} from './features/sales/customers/customer-form/customer-form.component';

export const routes: Routes = [

  // Rotas protegidas pelo MainLayout
  // Public Routes
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent, canActivate: [AuthGuard] },
  { path: 'forgot-password', component: ForgotPasswordComponent },
  { path: 'reset-password', component: ResetPasswordComponent, canActivate: [AuthGuard] },

  // Protected Routes within MainLayout
  {
    path: '',
    component: MainLayoutComponent,
    // canActivate: [AuthGuard],
    children: [
      { path: '', redirectTo: 'home', pathMatch: 'full' }, // Redireciona para /dashboard
      { path: 'dashboard', component: DashboardComponent },

      // Users
      { path: 'users', component: UserListComponent },
      { path: 'users/create', component: UserCreateComponent },
      { path: 'users/:id', component: UserDetailComponent },

      // Products
      { path: 'products', component: ProductListComponent },
      { path: 'products/create', component: ProductCreateComponent },
      { path: 'products/:id/edit', component: ProductEditComponent },
      { path: 'products/:id', component: ProductDetailComponent },

      // Customers
      { path: 'customers', component: CustomerListComponent },
      { path: 'customers/create', component: CustomerFormComponent },
      { path: 'customers/:id/edit', component: CustomerFormComponent },
      { path: 'customers/:id', component: CustomerDetailComponent },

      // Finance
      { path: 'finance/invoices', component: InvoicesComponent },
      { path: 'finance/payments', component: PaymentsComponent },
      { path: 'finance/reports', component: FinanceReportsComponent },

      // HR
      { path: 'hr/employees', component: EmployeesComponent },
      { path: 'hr/departments', component: DepartmentsComponent },

      // Inventory
      { path: 'inventory/products', component: ProductsComponent }, // Remova se já estiver listada acima
      { path: 'inventory/stock-levels', component: StockLevelsComponent },
      { path: 'inventory/transactions', component: TransactionsComponent },

      // Purchase
      { path: 'purchase/orders', component: PurchaseOrdersComponent },
      { path: 'purchase/suppliers', component: SuppliersComponent },

      // Reports
      { path: 'reports/sales', component: SalesReportComponent },
      { path: 'reports/inventory', component: InventoryReportComponent },
      { path: 'reports/finance', component: FinanceReportComponent },

      // Sales
      { path: 'sales/orders', component: SalesOrdersComponent },
      { path: 'sales/customers', component: CustomersComponent }, // Remova se já estiver listada acima
      { path: 'sales/quotations', component: QuotationsComponent },
    ]
  },

  // Wildcard Route
  { path: '**', redirectTo: '' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
