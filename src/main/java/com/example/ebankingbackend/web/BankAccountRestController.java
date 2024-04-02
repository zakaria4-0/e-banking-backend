package com.example.ebankingbackend.web;

import com.example.ebankingbackend.dto.BankAccountDTO;
import com.example.ebankingbackend.exceptions.AccountNotFoundException;
import com.example.ebankingbackend.exceptions.CustomerNotFoundException;
import com.example.ebankingbackend.service.BankAccountService;
import com.example.ebankingbackend.util.BankAccountRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class BankAccountRestController {
    private BankAccountService bankAccountService;

    @GetMapping("/bankAccounts")
    public List<BankAccountDTO> accountDTOList(){
        return bankAccountService.listBankAccounts();
    }

    @PostMapping("/bankAccount")
    public BankAccountDTO saveBankAccount(@RequestBody BankAccountRequest bankAccountRequest) throws CustomerNotFoundException {
        return bankAccountService.saveBankAccount(bankAccountRequest);
    }

    @GetMapping("/bankAccount/{id}")
    public BankAccountDTO getBankAccount(@PathVariable String id) throws AccountNotFoundException {
        return bankAccountService.getBankAccount(id);
    }

    @DeleteMapping("/bankAccount/{id}")
    public void deleteAccount(@PathVariable String id){
        bankAccountService.deleteAccount(id);
    }
}
