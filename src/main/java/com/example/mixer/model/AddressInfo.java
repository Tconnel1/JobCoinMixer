package com.example.mixer.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AddressInfo {
    private Long balance;
    private List<Transaction> transactions;
}
