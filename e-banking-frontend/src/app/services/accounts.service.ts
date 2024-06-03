import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {AccountDetails} from "../model/accountHistory.model";
import {OperationModel} from "../model/operation.model";
import {TransferModel} from "../model/transfer.model";
import {Account} from "../model/account.model";

@Injectable({
  providedIn: 'root'
})
export class AccountsService {
  private host : string = "http://localhost:8085/operations";

  constructor(private http : HttpClient) { }

  public getAccount(accountId : string, page : number, size : number):Observable<AccountDetails>{
    return this.http.get<AccountDetails>(this.host+"/"+accountId+"/pageOperations?page="+page+"&size="+size);
  }

  public debit(operation : OperationModel){
    return this.http.post(this.host+"/debit", operation);
  }

  credit(operation: OperationModel) {
    return this.http.post(this.host+"/credit", operation);
  }

  transfer(transferRequest: TransferModel) {
    return this.http.post(this.host+"/transfer", transferRequest)
  }

  public getCustomerAccounts(customerId : number): Observable<Array<Account>>{
    return this.http.get<Array<Account>>("http://localhost:8085/customerAccounts/"+customerId)
  }
}
