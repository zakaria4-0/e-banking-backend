package com.example.ebankingbackend.service;

import com.example.ebankingbackend.dto.AccountHistoryDTO;
import com.example.ebankingbackend.dto.BankAccountDTO;
import com.example.ebankingbackend.dto.CustomerDTO;
import com.example.ebankingbackend.exceptions.AccountNotFoundException;
import com.example.ebankingbackend.exceptions.BalanceNotSufficientException;
import com.example.ebankingbackend.exceptions.CustomerNotFoundException;
import com.example.ebankingbackend.util.BankAccountRequest;
import com.example.ebankingbackend.util.OperationRequest;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    BankAccountDTO saveBankAccount(BankAccountRequest bankAccountRequest) throws CustomerNotFoundException;

    List<CustomerDTO> listCustomers();
    BankAccountDTO getBankAccount(String accountId) throws AccountNotFoundException;
    void debit(OperationRequest operationRequest) throws AccountNotFoundException, BalanceNotSufficientException;
    void credit(OperationRequest operationRequest) throws AccountNotFoundException;
    void transfer(String accountIdSource, String accountIdDestination, Double amount) throws AccountNotFoundException, BalanceNotSufficientException;

    List<BankAccountDTO> listBankAccounts();

    CustomerDTO getCustomer(Long id) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long id);

    void deleteAccount(String id);
    AccountHistoryDTO getOperationsByAccountId(String accountId, int page, int size) throws AccountNotFoundException;
}
