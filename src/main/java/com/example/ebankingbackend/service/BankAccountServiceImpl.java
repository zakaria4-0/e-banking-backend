package com.example.ebankingbackend.service;

import com.example.ebankingbackend.dto.*;
import com.example.ebankingbackend.entities.*;
import com.example.ebankingbackend.enums.AccountStatus;
import com.example.ebankingbackend.enums.OperationType;
import com.example.ebankingbackend.exceptions.AccountNotFoundException;
import com.example.ebankingbackend.exceptions.BalanceNotSufficientException;
import com.example.ebankingbackend.exceptions.CustomerNotFoundException;
import com.example.ebankingbackend.mappers.BankAccountMapper;
import com.example.ebankingbackend.mappers.CustomerMapper;
import com.example.ebankingbackend.mappers.OperationMapper;
import com.example.ebankingbackend.repositories.BankAccountRepo;
import com.example.ebankingbackend.repositories.CustomerRepo;
import com.example.ebankingbackend.repositories.OperationRepo;
import com.example.ebankingbackend.util.BankAccountRequest;
import com.example.ebankingbackend.util.OperationRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{
    private CustomerRepo customerRepo;
    private BankAccountRepo bankAccountRepo;
    private OperationRepo operationRepo;
    private CustomerMapper customerMapper;
    private BankAccountMapper bankAccountMapper;
    private OperationMapper operationMapper;
    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("saving new customer");
        Customer customer = customerMapper.fromCustomerDTO(customerDTO);
        customerRepo.save(customer);
        return customerMapper.fromCustomer(customer);
    }

    @Override
    public BankAccountDTO saveBankAccount(BankAccountRequest bankAccountRequest) throws CustomerNotFoundException {
        log.info("saving new BankAccount");
        Customer customer = customerRepo.findById(bankAccountRequest.getCustomerId()).orElseThrow(()-> new CustomerNotFoundException("Customer Not found"));
        BankAccount bankAccount;
        if (bankAccountRequest.getType().equals("current")){
            bankAccount = new CurrentAccount();
        } else {
            bankAccount = new SavingAccount();
        }
        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setCreatedAt(new Date());
        bankAccount.setBalance(bankAccountRequest.getInitialBalance());
        bankAccount.setCustomer(customer);
        bankAccount.setStatus(AccountStatus.CREATED);
        if (bankAccount instanceof CurrentAccount currentAccount){
            currentAccount.setOverDraft(bankAccountRequest.getOverDraft());
            CurrentAccount savedAccount = bankAccountRepo.save(currentAccount);
            return bankAccountMapper.fromBankAccount(savedAccount);
        } else {
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            savingAccount.setInterestRate(bankAccountRequest.getInterestRate());
            SavingAccount savedAccount = bankAccountRepo.save(savingAccount);
            return bankAccountMapper.fromBankAccount(savedAccount);
        }
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<CustomerDTO> customerDTOS = customerRepo.findAll().stream().map(customer -> customerMapper.fromCustomer(customer)).toList();
        return customerDTOS;
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws AccountNotFoundException {
        BankAccount bankAccount = bankAccountRepo.findById(accountId).orElseThrow(() -> new AccountNotFoundException("Account with id " + accountId + " not found"));
        BankAccountDTO bankAccountDTO = bankAccountMapper.fromBankAccount(bankAccount);
        return bankAccountDTO;
    }

    @Override
    public void debit(OperationRequest operationRequest) throws AccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = bankAccountRepo.findById(operationRequest.getAccountId()).orElseThrow(() -> new AccountNotFoundException("Account with id " + operationRequest.getAccountId() + " not found"));
        if (bankAccount.getBalance() >= operationRequest.getAmount()) {
            bankAccount.setBalance(bankAccount.getBalance() - operationRequest.getAmount());
            bankAccountRepo.save(bankAccount);
            Operation operation = Operation.builder()
                    .amount(operationRequest.getAmount())
                    .operationDate(new Date())
                    .type(OperationType.DEBIT)
                    .description(operationRequest.getDescription())
                    .bankAccount(bankAccount)
                    .build();
            operationRepo.save(operation);
        } else throw new BalanceNotSufficientException("Balance not sufficient");
    }

    @Override
    public void credit(OperationRequest operationRequest) throws AccountNotFoundException {
        BankAccount bankAccount = bankAccountRepo.findById(operationRequest.getAccountId()).orElseThrow(() -> new AccountNotFoundException("Account with id " + operationRequest.getAccountId() + " not found"));
            bankAccount.setBalance(bankAccount.getBalance() + operationRequest.getAmount());
            bankAccountRepo.save(bankAccount);
        Operation operation = Operation.builder()
                .amount(operationRequest.getAmount())
                .operationDate(new Date())
                .type(OperationType.CREDIT)
                .description(operationRequest.getDescription())
                .bankAccount(bankAccount)
                .build();
        operationRepo.save(operation);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, Double amount) throws AccountNotFoundException, BalanceNotSufficientException {
        debit(new OperationRequest(accountIdSource,amount,"transfer from "+accountIdSource+" to "+accountIdDestination));
        credit(new OperationRequest(accountIdDestination, amount, "transfer from "+accountIdSource+" to "+accountIdDestination));
    }

    @Override
    public List<BankAccountDTO> listBankAccounts() {
        List<BankAccountDTO> bankAccountDTOS = bankAccountRepo.findAll().stream().map(account -> bankAccountMapper.fromBankAccount(account)).toList();
        return bankAccountDTOS;
    }

    @Override
    public CustomerDTO getCustomer(Long id) throws CustomerNotFoundException {
        Customer customer = customerRepo.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer with id " + id + " not found"));
        CustomerDTO customerDTO = customerMapper.fromCustomer(customer);
        return customerDTO;
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        Customer customer = customerMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepo.save(customer);
        return customerMapper.fromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepo.deleteById(id);
    }

    @Override
    public void deleteAccount(String id) {
        bankAccountRepo.deleteById(id);
    }

    @Override
    public AccountHistoryDTO getOperationsByAccountId(String accountId, int page, int size) throws AccountNotFoundException {
        BankAccount bankAccount = bankAccountRepo.findById(accountId).orElseThrow(() -> new AccountNotFoundException("Account which id is: " + accountId + " not found"));
        Page<Operation> operations = operationRepo.findByBankAccountId(accountId, PageRequest.of(page, size));
        List<OperationDTO> operationDTOS = operations.getContent().stream().map(operation -> operationMapper.fromOperation(operation)).toList();
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        accountHistoryDTO.setOperationDTOS(operationDTOS);
        accountHistoryDTO.setAccountId(accountId);
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(bankAccount.getOperationList().size()/size);
        return accountHistoryDTO;
    }
}
