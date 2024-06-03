import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Customer} from "../model/customer.model";
import {AccountsService} from "../services/accounts.service";
import {AccountDetails} from "../model/accountHistory.model";
import {Account} from "../model/account.model";

@Component({
  selector: 'app-customer-accounts',
  templateUrl: './customer-accounts.component.html',
  styleUrl: './customer-accounts.component.css'
})
export class CustomerAccountsComponent implements OnInit{
  customerId! : string;
  customer! : Customer ;
  bankAccounts! : Array<Account>;
  constructor(private route : ActivatedRoute, private router : Router, private accountService : AccountsService) {
    this.customer = router.getCurrentNavigation()?.extras.state as Customer;
  }

  ngOnInit(): void {
    this.customerId = this.route.snapshot.params['id']

    this.accountService.getCustomerAccounts(Number(this.customerId)).subscribe({
      next : data => {
        this.bankAccounts = data
        console.log(this.bankAccounts)
      },
      error : err => {

      }
    })
  }

}
