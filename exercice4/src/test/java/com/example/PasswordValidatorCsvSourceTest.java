package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PasswordValidatorCsvSourceTest {

    private PasswordValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PasswordValidator();
    }

    @ParameterizedTest(name = "{index} => password={0}, expected={1}")
    @CsvSource({
            "Password1!, true",
            "Admin2024@, true",
            "short1!, false",
            "PASSWORD1!, false",
            "password1!, false",
            "Password!, false",
            "Password1, false"
    })
    @DisplayName("Doit valider plusieurs mots de passe avec résultat attendu")
    void shouldValidatePasswordWithExpectedResult(String password, boolean expectedResult) {
        boolean result = validator.isValid(password);

        assertEquals(expectedResult, result);
    }

    @ParameterizedTest(name = "{index} => password={0}, expectedMessage={1}")
    @CsvSource({
            "Password1!, Password is valid",
            "short1!, Password must contain at least 8 characters",
            "PASSWORD1!, Password must contain at least one lowercase letter",
            "password1!, Password must contain at least one uppercase letter",
            "Password!, Password must contain at least one digit",
            "Password1, Password must contain at least one special character"
    })
    @DisplayName("Doit retourner le bon message d'erreur")
    void shouldReturnCorrectErrorMessage(String password, String expectedMessage) {
        String message = validator.getErrorMessage(password);

        assertEquals(expectedMessage, message);
    }
}
