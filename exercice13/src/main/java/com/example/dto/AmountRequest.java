package com.example.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AmountRequest(

        @NotNull(message = "Le montant est obligatoire")
        BigDecimal amount
) {}
