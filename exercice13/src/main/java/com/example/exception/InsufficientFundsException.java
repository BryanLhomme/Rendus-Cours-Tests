package com.example.exception;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String number) {
        super("Solde insuffisant sur le compte " + number);
    }
}
