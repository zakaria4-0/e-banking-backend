package com.example.ebankingbackend.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class TransferRequest {
    private String accountIdSource;
    private String accountIdDestination;
    private double amount;
}
