package com.example.mixer.enums;

public enum Status {
    AWAITING_DEPOSIT("Awaiting Deposit"),
    TRANSFERRING("Transferring"),
    COMPLETE("Complete");

    public final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }
}
