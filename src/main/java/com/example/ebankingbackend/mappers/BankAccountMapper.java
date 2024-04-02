package com.example.ebankingbackend.mappers;

import com.example.ebankingbackend.dto.BankAccountDTO;
import com.example.ebankingbackend.dto.CurrentAccountDTO;
import com.example.ebankingbackend.dto.SavingAccountDTO;
import com.example.ebankingbackend.entities.BankAccount;
import com.example.ebankingbackend.entities.CurrentAccount;
import com.example.ebankingbackend.entities.SavingAccount;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BankAccountMapper {
    CustomerMapper customerMapper;
    public BankAccountDTO fromBankAccount(BankAccount bankAccount){
        if (bankAccount instanceof CurrentAccount currentAccount){
            CurrentAccountDTO currentAccountDTO = new CurrentAccountDTO();
            BeanUtils.copyProperties(currentAccount, currentAccountDTO);
            currentAccountDTO.setCustomerDTO(customerMapper.fromCustomer(currentAccount.getCustomer()));
            currentAccountDTO.setType("CA");
            return currentAccountDTO;
        } else {
            SavingAccountDTO savingAccountDTO = new SavingAccountDTO();
            BeanUtils.copyProperties(bankAccount, savingAccountDTO);
            savingAccountDTO.setCustomerDTO(customerMapper.fromCustomer(bankAccount.getCustomer()));
            savingAccountDTO.setType("SA");
            return savingAccountDTO;
        }
    }

    public BankAccount fromBankAccountDTO(BankAccountDTO bankAccountDTO){
        if (bankAccountDTO instanceof CurrentAccountDTO currentAccountDTO){
            CurrentAccount currentAccount = new CurrentAccount();
            BeanUtils.copyProperties(currentAccountDTO, currentAccount);
            currentAccount.setCustomer(customerMapper.fromCustomerDTO(currentAccountDTO.getCustomerDTO()));
            return currentAccount;
        } else {
            SavingAccount savingAccount = new SavingAccount();
            BeanUtils.copyProperties(bankAccountDTO, savingAccount);
            savingAccount.setCustomer(customerMapper.fromCustomerDTO(bankAccountDTO.getCustomerDTO()));
            return savingAccount;
        }
    }
}
