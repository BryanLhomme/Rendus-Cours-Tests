package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordValidatorTest {

    private PasswordValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PasswordValidator();
    }

    @Test
    void shouldReturnTrueWhenPasswordIsValid() {
        // Arrange
        String password = "Password1!";

        // Act
        boolean result = validator.isValid(password);

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnTrueWhenPasswordContainsAtSymbol() {
        // Arrange
        String password = "Admin2024@";

        // Act
        boolean result = validator.isValid(password);

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenPasswordIsTooShort() {
        // Arrange
        String password = "short1!";

        // Act
        boolean result = validator.isValid(password);

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenPasswordHasNoLowercase() {
        // Arrange
        String password = "PASSWORD1!";

        // Act
        boolean result = validator.isValid(password);

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenPasswordHasNoUppercase() {
        // Arrange
        String password = "password1!";

        // Act
        boolean result = validator.isValid(password);

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenPasswordHasNoDigit() {
        // Arrange
        String password = "Password!";

        // Act
        boolean result = validator.isValid(password);

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenPasswordHasNoSpecialChar() {
        // Arrange
        String password = "Password1";

        // Act
        boolean result = validator.isValid(password);

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenPasswordIsNull() {
        // Act
        boolean result = validator.isValid(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnValidMessageWhenPasswordIsValid() {
        // Arrange
        String password = "Password1!";

        // Act
        String message = validator.getErrorMessage(password);

        // Assert
        assertEquals("Password is valid", message);
    }

    @Test
    void shouldReturnNullMessageWhenPasswordIsNull() {
        // Act
        String message = validator.getErrorMessage(null);

        // Assert
        assertEquals("Password must not be null", message);
    }

    @Test
    void shouldReturnTooShortMessageWhenPasswordIsTooShort() {
        // Arrange
        String password = "short1!";

        // Act
        String message = validator.getErrorMessage(password);

        // Assert
        assertEquals("Password must contain at least 8 characters", message);
    }

    @Test
    void shouldReturnNoLowercaseMessageWhenPasswordHasNoLowercase() {
        // Arrange
        String password = "PASSWORD1!";

        // Act
        String message = validator.getErrorMessage(password);

        // Assert
        assertEquals("Password must contain at least one lowercase letter", message);
    }

    @Test
    void shouldReturnNoUppercaseMessageWhenPasswordHasNoUppercase() {
        // Arrange
        String password = "password1!";

        // Act
        String message = validator.getErrorMessage(password);

        // Assert
        assertEquals("Password must contain at least one uppercase letter", message);
    }

    @Test
    void shouldReturnNoDigitMessageWhenPasswordHasNoDigit() {
        // Arrange
        String password = "Password!";

        // Act
        String message = validator.getErrorMessage(password);

        // Assert
        assertEquals("Password must contain at least one digit", message);
    }

    @Test
    void shouldReturnNoSpecialCharMessageWhenPasswordHasNoSpecialChar() {
        // Arrange
        String password = "Password1";

        // Act
        String message = validator.getErrorMessage(password);

        // Assert
        assertEquals("Password must contain at least one special character", message);
    }
}
