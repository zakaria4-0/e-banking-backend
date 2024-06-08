package com.example.ebankingbackend.web;

import com.example.ebankingbackend.dto.AccountHistoryDTO;
import com.example.ebankingbackend.dto.OperationDTO;
import com.example.ebankingbackend.exceptions.AccountNotFoundException;
import com.example.ebankingbackend.exceptions.BalanceNotSufficientException;
import com.example.ebankingbackend.service.BankAccountService;
import com.example.ebankingbackend.util.OperationRequest;
import com.example.ebankingbackend.util.TransferRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class OperationRestController {
    BankAccountService bankAccountService;

    @PostMapping("/admin/operations/debit")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public void debit(@RequestBody OperationRequest operationRequest) throws AccountNotFoundException, BalanceNotSufficientException {
        bankAccountService.debit(operationRequest);
    }

    @PostMapping("/admin/operations/credit")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public  void credit(@RequestBody OperationRequest operationRequest) throws AccountNotFoundException {
        bankAccountService.credit(operationRequest);
    }

    @PostMapping("/user/operations/transfer")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public void transfer(@RequestBody TransferRequest transferRequest) throws AccountNotFoundException, BalanceNotSufficientException {
        bankAccountService.transfer(transferRequest);
    }

    @GetMapping("/user/operations/{accountId}/pageOperations")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public AccountHistoryDTO getOperationsByAccountId(@PathVariable String accountId,
                                                            @RequestParam(name = "page", defaultValue = "0") int page,
                                                            @RequestParam(name = "size", defaultValue = "5") int size) throws AccountNotFoundException {
        return bankAccountService.getOperationsByAccountId(accountId, page, size);
    }
}
