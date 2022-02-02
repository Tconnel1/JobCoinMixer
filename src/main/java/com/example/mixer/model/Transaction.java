package com.example.mixer.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Transaction {
    private String timeStamp;
    private String fromAddress;
    private String toAddress;
    private String amount;

    public Transaction(String timeStamp, String toAddress, String amount) {
        this.timeStamp = timeStamp;
        this.toAddress = toAddress;
        this.amount = amount;
    }
}
