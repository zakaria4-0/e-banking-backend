package com.example.ebankingbackend.dto;

import com.example.ebankingbackend.enums.AccountStatus;
import lombok.Data;

import java.util.Date;

@Data
public class BankAccountDTO {
    private  String id;
    private String type;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
}
