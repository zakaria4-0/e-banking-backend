import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {CustomerService} from "../services/customer.service";
import {Customer} from "../model/customer.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {log} from "@angular-devkit/build-angular/src/builders/ssr-dev-server";
import {Router} from "@angular/router";

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.css'
})
export class CustomersComponent implements OnInit{
  customers! : Customer[];
  errorMsg? : string;
  searchFormGroup! : FormGroup;
  editCustomerFormGroup! : FormGroup;
  customer! : Customer;
  constructor(private customerService : CustomerService, private fb : FormBuilder, private router : Router) {
  }
  ngOnInit(): void {
    this.searchFormGroup=this.fb.group({
      keyword : this.fb.control("")
    });

    this.editCustomerFormGroup = this.fb.group({
      id : this.fb.control(""),
      name : this.fb.control("", [Validators.required, Validators.minLength(4)]),
      email : this.fb.control("", [Validators.required, Validators.email])
    })

   this.customerService.getCustomers().subscribe({
     next : data =>{
       this.customers = data
     },
     error : err => {
       this.errorMsg = err.message;
     }
   })
  }

  handleSearchCustomer() {
    let kw = this.searchFormGroup.value.keyword;
    this.customerService.searchCustomers(kw).subscribe({
      next : data => {
        this.customers = data
      },
      error : err => {
        this.errorMsg = err.message;
      }
    })
  }

  handleDeleteCustomer(c : Customer) {
    let conf = confirm("Do you confirm?")
    if (!conf) return ;
    this.customerService.deleteCustomer(c.id).subscribe({
      next : value => {
        let index : number = this.customers.indexOf(c);
        this.customers.splice(index,1)
        console.log(this.customers)
        alert("Customer has been deleted successfully")
      },
      error : err => {
        this.errorMsg="Cannot delete customer since it has accounts"
      }
    })
  }

  handleEditCustomer() {
    let customer : Customer = this.editCustomerFormGroup.value;
    this.customerService.editCustomer(customer).subscribe({
      next : data => {
        let index : number = this.customers.indexOf(this.customer);
        console.log(index+": "+this.customer.name)
        this.customers[index] = customer;
        this.customer = customer;
        alert("Customer infos edited successfully")

      },
      error : err => {

      }
    })
  }

  handleEditCustomerFormGroup(c: Customer) {
    this.customer = c;
    this.editCustomerFormGroup = this.fb.group({
      id : this.fb.control(c.id),
      name : this.fb.control(c.name, [Validators.required, Validators.minLength(4)]),
      email : this.fb.control(c.email, [Validators.required, Validators.email])
    })
  }

  handleCustomerAccounts(c: Customer) {
    this.router.navigateByUrl("/customerAccounts/"+c.id, {state : c});
  }
}
