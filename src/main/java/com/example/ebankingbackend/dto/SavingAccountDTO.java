package com.example.ebankingbackend.dto;

import com.example.ebankingbackend.enums.AccountStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class SavingAccountDTO extends BankAccountDTO{
    private double interestRate;
}
