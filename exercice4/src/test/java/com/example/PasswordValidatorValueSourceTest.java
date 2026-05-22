package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordValidatorValueSourceTest {

    private PasswordValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PasswordValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Password1!",
            "Admin2024@",
            "Secure9#Pass"
    })
    @DisplayName("Doit accepter plusieurs mots de passe valides")
    void shouldReturnTrueWhenPasswordsAreValid(String password) {
        boolean result = validator.isValid(password);

        assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "short1!",
            "PASSWORD1!",
            "password1!",
            "Password!",
            "Password1"
    })
    @DisplayName("Doit rejeter plusieurs mots de passe invalides")
    void shouldReturnFalseWhenPasswordsAreInvalid(String password) {
        boolean result = validator.isValid(password);

        assertFalse(result);
    }
}
