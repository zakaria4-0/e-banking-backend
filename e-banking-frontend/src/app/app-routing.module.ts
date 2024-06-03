import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CustomersComponent} from "./customers/customers.component";
import {AccountsComponent} from "./accounts/accounts.component";
import {NewCustomerComponent} from "./new-customer/new-customer.component";
import {CustomerAccountsComponent} from "./customer-accounts/customer-accounts.component";
import {LoginComponent} from "./login/login.component";
import {AdminTemplateComponent} from "./admin-template/admin-template.component";

const routes: Routes = [
  {path :"admin", component : AdminTemplateComponent, children: [
      {path :"customers", component : CustomersComponent},
      {path :"accounts", component : AccountsComponent},
      {path :"newCustomer", component : NewCustomerComponent},
      {path :"customerAccounts/:id", component : CustomerAccountsComponent}
    ]},
  {path : "login", component : LoginComponent},
  {path : "", redirectTo : "/login", pathMatch:"full"}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
