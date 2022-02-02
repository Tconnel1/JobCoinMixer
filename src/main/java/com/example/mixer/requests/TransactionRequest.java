package com.example.mixer.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TransactionRequest {
    private String fromAddress;
    private String toAddress;
    private String amount;
}
