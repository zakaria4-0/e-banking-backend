import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CustomersComponent} from "./customers/customers.component";
import {AccountsComponent} from "./accounts/accounts.component";
import {NewCustomerComponent} from "./new-customer/new-customer.component";
import {CustomerAccountsComponent} from "./customer-accounts/customer-accounts.component";
import {LoginComponent} from "./login/login.component";
import {AdminTemplateComponent} from "./admin-template/admin-template.component";
import {authenticationGuard} from "./guards/authentication.guard";
import {authorizationGuard} from "./guards/authorization.guard";
import {UnauthorizedComponent} from "./unauthorized/unauthorized.component";

const routes: Routes = [
  {path :"admin", component : AdminTemplateComponent,canActivate : [authenticationGuard], children: [
      {path :"customers", component : CustomersComponent, canActivate : [authorizationGuard]},
      {path :"accounts", component : AccountsComponent},
      {path :"newCustomer", component : NewCustomerComponent},
      {path :"customerAccounts/:id", component : CustomerAccountsComponent},
      {path :"unauthorized", component : UnauthorizedComponent}
    ]},
  {path : "login", component : LoginComponent},
  {path : "", redirectTo : "/login", pathMatch:"full"}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
