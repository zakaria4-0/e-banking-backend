import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {AccountsService} from "../services/accounts.service";
import {AccountDetails} from "../model/accountHistory.model";
import {OperationModel} from "../model/operation.model";
import {TransferModel} from "../model/transfer.model";

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrl: './accounts.component.css'
})
export class AccountsComponent implements OnInit{
  searchAccountFormGroup! : FormGroup;
  currentPage : number = 0;
  pageSize : number = 5;
  accountHistory! : AccountDetails | null;
  operationsFormGroup! : FormGroup;
  errorMessage! : string;
  constructor(private fb : FormBuilder, private accountService : AccountsService) {
  }
  ngOnInit(): void {
    this.searchAccountFormGroup=this.fb.group({
      accountId : this.fb.control("")
    });

    this.operationsFormGroup=this.fb.group({
      operationType : this.fb.control(null),
      amount : this.fb.control(0),
      description : this.fb.control(null),
      accountIdDestination : this.fb.control(null)
    })
  }


  handleSearchAccount() {
    this.accountHistory=null;
    let accountId : string = this.searchAccountFormGroup.value.accountId
    this.accountService.getAccount(accountId, this.currentPage, this.pageSize).subscribe({
      next : data => {
        this.accountHistory = data;
      },
      error : err => {
        this.errorMessage = err.message;
        console.log("error message: "+this.errorMessage);
      }
    })
  }

  goToPage(page: number) {
    this.currentPage = page;
    this.handleSearchAccount();
  }

  handleAccountOperation() {
    if (this.accountHistory != null) {
      let accountId : string = this.accountHistory.accountId;
      let operation : OperationModel = this.operationsFormGroup.value;
      operation.accountId = accountId;
      let operationType = this.operationsFormGroup.value.operationType;
      if (operationType=='DEBIT'){
        this.accountService.debit(operation).subscribe({
          next : value => {
            alert("operation has been executed");
            this.operationsFormGroup.reset();
            this.handleSearchAccount()
          },
          error : err => { console.log(err)}
        });
      }
      if (operationType=='CREDIT'){
        this.accountService.credit(operation).subscribe({
          next : value => {
            alert("operation has been executed");
            this.operationsFormGroup.reset();
            this.handleSearchAccount()
          },
          error : err => { console.log(err)}
        });
      }
      if (operationType=='TRANSFER'){
        let transferRequest : TransferModel = this.operationsFormGroup.value;
        transferRequest.accountIdSource = accountId;
        this.accountService.transfer(transferRequest).subscribe({
          next : value => {
            alert("operation has been executed");
            this.operationsFormGroup.reset();
            this.handleSearchAccount()
          },
          error : err => { console.log(err)}
        });
      }
    }

  }
}
