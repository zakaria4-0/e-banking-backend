package com.example.ebankingbackend.web;

import com.example.ebankingbackend.dto.AccountHistoryDTO;
import com.example.ebankingbackend.dto.OperationDTO;
import com.example.ebankingbackend.exceptions.AccountNotFoundException;
import com.example.ebankingbackend.exceptions.BalanceNotSufficientException;
import com.example.ebankingbackend.service.BankAccountService;
import com.example.ebankingbackend.util.OperationRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class OperationRestController {
    BankAccountService bankAccountService;

    @PostMapping("/debit")
    public void debit(OperationRequest operationRequest) throws AccountNotFoundException, BalanceNotSufficientException {
        bankAccountService.debit(operationRequest);
    }

    @PostMapping("/credit")
    public  void credit(OperationRequest operationRequest) throws AccountNotFoundException {
        bankAccountService.credit(operationRequest);
    }

    @PostMapping("/transfer/{accountIdSource}/{accountIdDestination}/{amount}")
    public void transfer(@PathVariable String accountIdSource, @PathVariable String accountIdDestination, @PathVariable double amount) throws AccountNotFoundException, BalanceNotSufficientException {
        bankAccountService.transfer(accountIdSource, accountIdDestination, amount);
    }

    @GetMapping("/operations/{accountId}/pageOperations")
    public AccountHistoryDTO getOperationsByAccountId(@PathVariable String accountId,
                                                            @RequestParam(name = "page", defaultValue = "0") int page,
                                                            @RequestParam(name = "size", defaultValue = "5") int size) throws AccountNotFoundException {
        return bankAccountService.getOperationsByAccountId(accountId, page, size);
    }
}
