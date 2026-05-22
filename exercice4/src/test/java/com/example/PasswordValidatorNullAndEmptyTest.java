package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class PasswordValidatorNullAndEmptyTest {

    private PasswordValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PasswordValidator();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Doit rejeter un mot de passe null ou vide")
    void shouldReturnFalseWhenPasswordIsNullOrEmpty(String password) {
        boolean result = validator.isValid(password);

        assertFalse(result);
    }
}
