package com.example.service;

import com.example.exception.AccountAlreadyExistsException;
import com.example.exception.AccountNotFoundException;
import com.example.exception.InsufficientFundsException;
import com.example.model.Account;
import com.example.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests du service AccountService")
class AccountServiceTest {

    @Mock
    private AccountRepository repository;

    @InjectMocks
    private AccountService accountService;

    // =====================================================================
    // Création de compte
    // =====================================================================

    @Test
    @DisplayName("La création d'un compte valide doit retourner le compte avec un solde à zéro")
    void shouldCreateAccountWithZeroBalance() {
        // Arrange
        Account saved = new Account("FR001", "Alice Martin");
        when(repository.existsByNumber("FR001")).thenReturn(false);
        when(repository.save(any(Account.class))).thenReturn(saved);

        // Act
        Account result = accountService.create("FR001", "Alice Martin");

        // Assert
        assertEquals("FR001", result.getNumber());
        assertEquals("Alice Martin", result.getHolder());
        assertEquals(0, result.getBalance().compareTo(BigDecimal.ZERO));
        verify(repository).save(any(Account.class));
    }

    @Test
    @DisplayName("La création d'un compte avec un numéro déjà existant doit échouer")
    void shouldThrowExceptionWhenAccountNumberAlreadyExists() {
        // Arrange
        when(repository.existsByNumber("FR001")).thenReturn(true);

        // Act & Assert
        assertThrows(AccountAlreadyExistsException.class,
                () -> accountService.create("FR001", "Alice Martin"));
        verify(repository, never()).save(any());
    }

    // =====================================================================
    // Consultation
    // =====================================================================

    @Test
    @DisplayName("La récupération d'un compte existant doit retourner le compte")
    void shouldReturnAccountWhenFound() {
        // Arrange
        Account account = new Account("FR001", "Alice Martin");
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(account));

        // Act
        Account result = accountService.getByNumber("FR001");

        // Assert
        assertEquals("FR001", result.getNumber());
        verify(repository).findByNumber("FR001");
    }

    @Test
    @DisplayName("La récupération d'un compte introuvable doit lancer une exception")
    void shouldThrowExceptionWhenAccountNotFound() {
        // Arrange
        when(repository.findByNumber("FR999")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccountNotFoundException.class,
                () -> accountService.getByNumber("FR999"));
        verify(repository).findByNumber("FR999");
    }

    @Test
    @DisplayName("La récupération de tous les comptes doit retourner la liste complète")
    void shouldReturnAllAccounts() {
        // Arrange
        List<Account> accounts = List.of(
                new Account("FR001", "Alice Martin"),
                new Account("FR002", "Bob Dupont")
        );
        when(repository.findAll()).thenReturn(accounts);

        // Act
        List<Account> result = accountService.findAll();

        // Assert
        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    // =====================================================================
    // Dépôt
    // =====================================================================

    @Test
    @DisplayName("Un dépôt valide doit augmenter le solde du compte")
    void shouldIncreaseBalanceOnValidDeposit() {
        // Arrange
        Account account = new Account("FR001", "Alice Martin");
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(account));
        when(repository.save(account)).thenReturn(account);

        // Act
        Account result = accountService.deposit("FR001", new BigDecimal("100.00"));

        // Assert
        assertEquals(new BigDecimal("100.00"), result.getBalance());
        verify(repository).save(account);
    }

    @Test
    @DisplayName("Un dépôt d'un montant nul doit être refusé")
    void shouldThrowExceptionWhenDepositAmountIsZero() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> accountService.deposit("FR001", BigDecimal.ZERO));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Un dépôt d'un montant négatif doit être refusé")
    void shouldThrowExceptionWhenDepositAmountIsNegative() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> accountService.deposit("FR001", new BigDecimal("-50.00")));
        verify(repository, never()).save(any());
    }

    // =====================================================================
    // Retrait
    // =====================================================================

    @Test
    @DisplayName("Un retrait valide doit diminuer le solde du compte")
    void shouldDecreaseBalanceOnValidWithdraw() {
        // Arrange
        Account account = new Account("FR001", "Alice Martin");
        account.setBalance(new BigDecimal("200.00"));
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(account));
        when(repository.save(account)).thenReturn(account);

        // Act
        Account result = accountService.withdraw("FR001", new BigDecimal("80.00"));

        // Assert
        assertEquals(new BigDecimal("120.00"), result.getBalance());
        verify(repository).save(account);
    }

    @Test
    @DisplayName("Un retrait d'un montant nul doit être refusé")
    void shouldThrowExceptionWhenWithdrawAmountIsZero() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> accountService.withdraw("FR001", BigDecimal.ZERO));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Un retrait d'un montant négatif doit être refusé")
    void shouldThrowExceptionWhenWithdrawAmountIsNegative() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> accountService.withdraw("FR001", new BigDecimal("-30.00")));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Un retrait avec des fonds insuffisants doit être refusé")
    void shouldThrowExceptionWhenInsufficientFundsOnWithdraw() {
        // Arrange
        Account account = new Account("FR001", "Alice Martin");
        account.setBalance(new BigDecimal("50.00"));
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(account));

        // Act & Assert
        assertThrows(InsufficientFundsException.class,
                () -> accountService.withdraw("FR001", new BigDecimal("100.00")));
        verify(repository, never()).save(any());
    }

    // =====================================================================
    // Virement
    // =====================================================================

    @Test
    @DisplayName("Un virement valide doit débiter l'émetteur et créditer le destinataire")
    void shouldTransferAmountBetweenAccounts() {
        // Arrange
        Account from = new Account("FR001", "Alice Martin");
        from.setBalance(new BigDecimal("500.00"));
        Account to = new Account("FR002", "Bob Dupont");
        to.setBalance(new BigDecimal("100.00"));
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(from));
        when(repository.findByNumber("FR002")).thenReturn(Optional.of(to));
        when(repository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        accountService.transfer("FR001", "FR002", new BigDecimal("200.00"));

        // Assert
        assertEquals(new BigDecimal("300.00"), from.getBalance());
        assertEquals(new BigDecimal("300.00"), to.getBalance());
        verify(repository).save(from);
        verify(repository).save(to);
    }

    @Test
    @DisplayName("Un virement d'un montant nul doit être refusé")
    void shouldThrowExceptionWhenTransferAmountIsZero() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> accountService.transfer("FR001", "FR002", BigDecimal.ZERO));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Un virement d'un montant négatif doit être refusé")
    void shouldThrowExceptionWhenTransferAmountIsNegative() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> accountService.transfer("FR001", "FR002", new BigDecimal("-100.00")));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Un virement avec des fonds insuffisants doit être refusé")
    void shouldThrowExceptionWhenInsufficientFundsOnTransfer() {
        // Arrange
        Account from = new Account("FR001", "Alice Martin");
        from.setBalance(new BigDecimal("50.00"));
        Account to = new Account("FR002", "Bob Dupont");
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(from));
        when(repository.findByNumber("FR002")).thenReturn(Optional.of(to));

        // Act & Assert
        assertThrows(InsufficientFundsException.class,
                () -> accountService.transfer("FR001", "FR002", new BigDecimal("100.00")));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Un virement depuis un compte inexistant doit être refusé")
    void shouldThrowExceptionWhenSourceAccountNotFound() {
        // Arrange
        when(repository.findByNumber("FR999")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccountNotFoundException.class,
                () -> accountService.transfer("FR999", "FR002", new BigDecimal("100.00")));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Un virement vers un compte inexistant doit être refusé")
    void shouldThrowExceptionWhenTargetAccountNotFound() {
        // Arrange
        Account from = new Account("FR001", "Alice Martin");
        from.setBalance(new BigDecimal("500.00"));
        when(repository.findByNumber("FR001")).thenReturn(Optional.of(from));
        when(repository.findByNumber("FR999")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccountNotFoundException.class,
                () -> accountService.transfer("FR001", "FR999", new BigDecimal("100.00")));
        verify(repository, never()).save(any());
    }
}
