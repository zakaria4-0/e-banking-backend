package com.example.ebankingbackend.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class OperationRequest {
    private String accountId;
    private double amount;
    private String description;
}
