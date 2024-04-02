package com.example.ebankingbackend.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class BankAccountRequest {
    private double initialBalance;
    private String type;
    private Long customerId;
    private double interestRate;
    private double overDraft;
}
