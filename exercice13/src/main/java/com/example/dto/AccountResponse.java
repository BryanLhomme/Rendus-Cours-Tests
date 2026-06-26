package com.example.dto;

import com.example.model.Account;

import java.math.BigDecimal;

public record AccountResponse(String number, String holder, BigDecimal balance) {

    public static AccountResponse from(Account account) {
        return new AccountResponse(
                account.getNumber(),
                account.getHolder(),
                account.getBalance()
        );
    }
}
