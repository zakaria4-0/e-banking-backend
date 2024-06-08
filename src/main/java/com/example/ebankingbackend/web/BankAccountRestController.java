package com.example.ebankingbackend.web;

import com.example.ebankingbackend.dto.BankAccountDTO;
import com.example.ebankingbackend.exceptions.AccountNotFoundException;
import com.example.ebankingbackend.exceptions.CustomerNotFoundException;
import com.example.ebankingbackend.service.BankAccountService;
import com.example.ebankingbackend.util.BankAccountRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class BankAccountRestController {
    private BankAccountService bankAccountService;

    @GetMapping("/admin/bankAccounts")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public List<BankAccountDTO> accountDTOList(){
        return bankAccountService.listBankAccounts();
    }

    @PostMapping("/admin/bankAccount")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public BankAccountDTO saveBankAccount(@RequestBody BankAccountRequest bankAccountRequest) throws CustomerNotFoundException {
        return bankAccountService.saveBankAccount(bankAccountRequest);
    }

    @GetMapping("/admin/bankAccount/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public BankAccountDTO getBankAccount(@PathVariable String id) throws AccountNotFoundException {
        return bankAccountService.getBankAccount(id);
    }

    @GetMapping("/admin/customerAccounts/{customerId}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public List<BankAccountDTO> getCustomerAccounts(@PathVariable Long customerId){
        return bankAccountService.getAccountsByCustomerId(customerId);
    }

    @DeleteMapping("/admin/bankAccount/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public void deleteAccount(@PathVariable String id){
        bankAccountService.deleteAccount(id);
    }
}
