package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PriceCalculatorTest {

    private final PriceCalculator calculator = new PriceCalculator();


    @Test
    void shouldReturnTotalPriceWhenUnitPriceIs10AndQuantityIs3() {
        // Arrange
        double unitPrice = 10.0;
        int quantity = 3;

        // Act
        double result = calculator.calculateTotalPrice(unitPrice, quantity);

        // Assert
        assertEquals(30.0, result);
    }

    @Test
    void shouldReturnZeroWhenQuantityIsZero() {
        // Arrange
        double unitPrice = 10.0;
        int quantity = 0;

        // Act
        double result = calculator.calculateTotalPrice(unitPrice, quantity);

        // Assert
        assertEquals(0.0, result);
    }

    @Test
    void shouldThrowWhenUnitPriceIsNegative() {
        // Arrange
        double unitPrice = -1.0;
        int quantity = 3;

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> calculator.calculateTotalPrice(unitPrice, quantity));
    }

    @Test
    void shouldThrowWhenQuantityIsNegative() {
        // Arrange
        double unitPrice = 10.0;
        int quantity = -1;

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> calculator.calculateTotalPrice(unitPrice, quantity));
    }


    @Test
    void shouldReturn80WhenPriceIs100AndDiscountIs20Percent() {
        // Arrange
        double price = 100.0;
        double discountRate = 0.20;

        // Act
        double result = calculator.applyDiscount(price, discountRate);

        // Assert
        assertEquals(80.0, result);
    }

    @Test
    void shouldReturnFullPriceWhenDiscountIsZero() {
        // Arrange
        double price = 100.0;
        double discountRate = 0.0;

        // Act
        double result = calculator.applyDiscount(price, discountRate);

        // Assert
        assertEquals(100.0, result);
    }

    @Test
    void shouldThrowWhenDiscountRateIsNegative() {
        // Arrange
        double price = 100.0;
        double discountRate = -0.10;

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> calculator.applyDiscount(price, discountRate));
    }


    @Test
    void shouldReturn20WhenPriceIs100AndVatIs20Percent() {
        // Arrange
        double price = 100.0;
        double vatRate = 0.20;

        // Act
        double result = calculator.calculateVat(price, vatRate);

        // Assert
        assertEquals(20.0, result);
    }

    @Test
    void shouldReturnZeroVatWhenVatRateIsZero() {
        // Arrange
        double price = 100.0;
        double vatRate = 0.0;

        // Act
        double result = calculator.calculateVat(price, vatRate);

        // Assert
        assertEquals(0.0, result);
    }

    @Test
    void shouldThrowWhenVatRateIsNegativeInCalculateVat() {
        // Arrange
        double price = 100.0;
        double vatRate = -0.20;

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> calculator.calculateVat(price, vatRate));
    }


    @Test
    void shouldReturn120WhenPriceIs100AndVatIs20Percent() {
        // Arrange
        double price = 100.0;
        double vatRate = 0.20;

        // Act
        double result = calculator.calculatePriceWithVat(price, vatRate);

        // Assert
        assertEquals(120.0, result);
    }

    @Test
    void shouldReturnUnchangedPriceWhenVatIsZero() {
        // Arrange
        double price = 100.0;
        double vatRate = 0.0;

        // Act
        double result = calculator.calculatePriceWithVat(price, vatRate);

        // Assert
        assertEquals(100.0, result);
    }

    @Test
    void shouldThrowWhenVatRateIsNegativeInCalculatePriceWithVat() {
        // Arrange
        double price = 100.0;
        double vatRate = -0.20;

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> calculator.calculatePriceWithVat(price, vatRate));
    }
}
