package com.example.exception;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String number) {
        super("Compte " + number + " introuvable");
    }
}
