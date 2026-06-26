package com.example.controller;

import com.example.dto.AccountResponse;
import com.example.dto.AmountRequest;
import com.example.dto.CreateAccountRequest;
import com.example.dto.TransferRequest;
import com.example.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/**
 * Couche web REST — API de gestion des comptes bancaires.
 *
 * @RestController indique que les méthodes renvoient directement des réponses HTTP/JSON.
 * @RequestMapping définit le préfixe commun des URLs de ce contrôleur.
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    /**
     * POST /api/accounts
     * Crée un nouveau compte bancaire.
     *
     * @Valid déclenche la validation des annotations présentes dans CreateAccountRequest.
     * Retourne 201 Created avec l'URL du compte dans le header Location.
     */
    @PostMapping
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody CreateAccountRequest request) {
        var account = service.create(request.number(), request.holder());
        var response = AccountResponse.from(account);

        return ResponseEntity
                .created(URI.create("/api/accounts/" + response.number()))
                .body(response);
    }

    /**
     * GET /api/accounts
     * Retourne la liste de tous les comptes.
     */
    @GetMapping
    public ResponseEntity<List<AccountResponse>> findAll() {
        var responses = service.findAll()
                .stream()
                .map(AccountResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }

    /**
     * GET /api/accounts/{number}
     * Retourne un compte par son numéro. Retourne 404 s'il est introuvable.
     *
     * @PathVariable récupère la valeur dynamique présente dans l'URL.
     */
    @GetMapping("/{number}")
    public ResponseEntity<AccountResponse> getByNumber(@PathVariable String number) {
        var account = service.getByNumber(number);
        return ResponseEntity.ok(AccountResponse.from(account));
    }

    /**
     * POST /api/accounts/{number}/deposit
     * Effectue un dépôt sur un compte. Le montant doit être strictement positif.
     */
    @PostMapping("/{number}/deposit")
    public ResponseEntity<AccountResponse> deposit(
            @PathVariable String number,
            @Valid @RequestBody AmountRequest request) {
        var account = service.deposit(number, request.amount());
        return ResponseEntity.ok(AccountResponse.from(account));
    }

    /**
     * POST /api/accounts/{number}/withdraw
     * Effectue un retrait sur un compte. Retourne 409 si le solde est insuffisant.
     */
    @PostMapping("/{number}/withdraw")
    public ResponseEntity<AccountResponse> withdraw(
            @PathVariable String number,
            @Valid @RequestBody AmountRequest request) {
        var account = service.withdraw(number, request.amount());
        return ResponseEntity.ok(AccountResponse.from(account));
    }

    /**
     * POST /api/accounts/transfer
     * Effectue un virement entre deux comptes. Retourne 409 si le solde est insuffisant.
     */
    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@Valid @RequestBody TransferRequest request) {
        service.transfer(request.fromNumber(), request.toNumber(), request.amount());
        return ResponseEntity.ok().build();
    }
}
