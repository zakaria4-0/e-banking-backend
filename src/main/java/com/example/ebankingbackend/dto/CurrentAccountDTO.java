package com.example.ebankingbackend.dto;

import com.example.ebankingbackend.entities.Customer;
import com.example.ebankingbackend.entities.Operation;
import com.example.ebankingbackend.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CurrentAccountDTO extends  BankAccountDTO{
    private double overDraft;
}
