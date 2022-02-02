package com.example.mixer.model;

import com.example.mixer.enums.Status;
import lombok.Data;

import java.util.Set;

@Data
public class MixerRecord {
    private String depositAddress; //this should be unique
    private Set<String> toAddressList;
    private Double balance;
    private Status status;
    private Double completion;

    public MixerRecord(String depositAddress, Set<String> toAddressList) {
        this.depositAddress = depositAddress;
        this.toAddressList = toAddressList;
        this.balance = 0.00;
        this.status = Status.AWAITING_DEPOSIT;
        this.completion = 0.00;

    }
}

