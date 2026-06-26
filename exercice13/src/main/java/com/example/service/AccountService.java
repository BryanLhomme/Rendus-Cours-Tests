package com.example.service;

import com.example.exception.AccountAlreadyExistsException;
import com.example.exception.AccountNotFoundException;
import com.example.exception.InsufficientFundsException;
import com.example.model.Account;
import com.example.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public Account create(String number, String holder) {
        if (repository.existsByNumber(number)) {
            throw new AccountAlreadyExistsException(number);
        }
        return repository.save(new Account(number, holder));
    }

    public Account getByNumber(String number) {
        return repository.findByNumber(number)
                .orElseThrow(() -> new AccountNotFoundException(number));
    }

    public List<Account> findAll() {
        return repository.findAll();
    }

    public Account deposit(String number, BigDecimal amount) {
        validatePositiveAmount(amount);
        Account account = getByNumber(number);
        account.setBalance(account.getBalance().add(amount));
        return repository.save(account);
    }

    public Account withdraw(String number, BigDecimal amount) {
        validatePositiveAmount(amount);
        Account account = getByNumber(number);
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(number);
        }
        account.setBalance(account.getBalance().subtract(amount));
        return repository.save(account);
    }

    public void transfer(String fromNumber, String toNumber, BigDecimal amount) {
        validatePositiveAmount(amount);
        Account from = getByNumber(fromNumber);
        Account to = getByNumber(toNumber);
        if (from.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(fromNumber);
        }
        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
        repository.save(from);
        repository.save(to);
    }

    private void validatePositiveAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être strictement positif");
        }
    }
}
