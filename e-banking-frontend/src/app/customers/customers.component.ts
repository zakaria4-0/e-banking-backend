import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {CustomerService} from "../services/customer.service";
import {Customer} from "../model/customer.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {log} from "@angular-devkit/build-angular/src/builders/ssr-dev-server";
import {Router} from "@angular/router";
import {AccountsService} from "../services/accounts.service";
import {Account} from "../model/account.model";

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.css'
})
export class CustomersComponent implements OnInit{
  customersByPage! : Customer[];
  customers! : Customer[];
  errorMsg? : string;
  searchFormGroup! : FormGroup;
  editCustomerFormGroup! : FormGroup;
  customer! : Customer;
  totalPages!: number;
  pageSize: number = 5;
  currentPage: number=0;
  customerAccounts!: Account[]
  currentPages: number[]=[];
  constructor(private customerService : CustomerService, private fb : FormBuilder, private router : Router,
              private accountService: AccountsService) {
  }
  ngOnInit(): void {
    this.searchFormGroup=this.fb.group({
      keyword : this.fb.control("")
    });

    this.searchFormGroup.valueChanges.subscribe(value=>{
      this.handleSearchCustomer();
    })

    this.editCustomerFormGroup = this.fb.group({
      id : this.fb.control(""),
      name : this.fb.control("", [Validators.required, Validators.minLength(4)]),
      email : this.fb.control("", [Validators.required, Validators.email])
    })

   this.customerService.getCustomers().subscribe({
     next : data =>{
       this.customers = data
       this.handleChangePageSize(this.pageSize.toString());
       this.goToNextPageList(0)
     },
     error : err => {
       this.errorMsg = err.message;
     }
   })
  }

  goToNextPageList(index: number){
    debugger;
    this.currentPages=[]
    for (let i =0; i<this.totalPages; i++){
      this.currentPages.push(i)
    }
    this.currentPages=this.currentPages.slice(index, index+3)
    console.log("currentPages "+this.currentPages)
    console.log("include total "+this.currentPages.includes(this.totalPages-1))
  }

  goToPreviousPageList(index: number){
    debugger
    this.currentPages=[]
    for (let i =0; i<this.totalPages; i++){
      this.currentPages.push(i)
    }
    if (this.currentPages[index]){
      this.currentPages=this.currentPages.slice(index-3, index)
    }else {
      this.currentPages=this.currentPages.slice(index-3, this.currentPages.length)
    }



  }

  getCustomersListPage(pageSize: number, currentPage: number){
    this.customersByPage=this.customers
    if (currentPage>this.totalPages-1){
      currentPage=this.totalPages-1
    }
    debugger;
    let index : number =  pageSize + (pageSize * currentPage);
    console.log("***** index: "+index)
    if (this.customersByPage[index]){
      this.customersByPage=this.customersByPage.slice(currentPage*pageSize, index)
      this.customersByPage.forEach(c=> console.log("***** returned customers: "+c.id))
    }else {
      this.customersByPage=this.customersByPage.slice(currentPage*pageSize, this.customers.length)
    }

  }

  goToPage(page: number){
    this.currentPage=page;
    this.getCustomersListPage(parseInt(this.pageSize.toString()), this.currentPage)
  }

  handleSearchCustomer() {
    let kw = this.searchFormGroup.value.keyword;
    this.customerService.searchCustomers(kw).subscribe({
      next : data => {
        this.customers = data
        this.handleChangePageSize(this.pageSize.toString())
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
        this.getCustomersListPage(this.pageSize, this.currentPage)
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

  handleChangePageSize(pageSize: string) {
    let page: number = parseInt(pageSize)
    if (this.customers.length % page != 0){
      this.totalPages = Math.floor(this.customers.length / page) + 1
    } else {
      this.totalPages = this.customers.length / page
    }
    console.log("******* totalPages= "+this.totalPages)
    console.log("**** length= "+this.customers.length)
    console.log("**** pageSize= "+page)
    console.log("***** Modulo= "+this.customers.length % page)
    this.getCustomersListPage(page,this.currentPage);
  }

  handleGetCustomerAccounts(id: number) {
    this.accountService.getCustomerAccounts(id).subscribe(
      results=>{
        this.customerAccounts=results;
      }
    )
  }

  handleGoToAccountHistory() {
    this.router.navigateByUrl("/accounts")
  }
}
