package com.example.model;

import java.math.BigDecimal;

public class Account {

    private String number;
    private String holder;
    private BigDecimal balance;

    public Account(String number, String holder) {
        this.number = number;
        this.holder = holder;
        this.balance = BigDecimal.ZERO;
    }

    public String getNumber() {
        return number;
    }

    public String getHolder() {
        return holder;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
