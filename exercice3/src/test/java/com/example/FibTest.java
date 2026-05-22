package com.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FibTest {

    @Test
    void shouldReturnNonEmptyListWhenRangeIsOne() {
        // Arrange
        Fib fib = new Fib(1);

        // Act
        List<Integer> result = fib.getFibSeries();

        // Assert
        assertFalse(result.isEmpty());
    }

    @Test
    void shouldReturnListContainingZeroWhenRangeIsOne() {
        // Arrange
        Fib fib = new Fib(1);

        // Act
        List<Integer> result = fib.getFibSeries();

        // Assert
        assertEquals(List.of(0), result);
    }

    @Test
    void shouldContainThreeWhenRangeIsSix() {
        // Arrange
        Fib fib = new Fib(6);

        // Act
        List<Integer> result = fib.getFibSeries();

        // Assert
        assertTrue(result.contains(3));
    }

    @Test
    void shouldContainSixElementsWhenRangeIsSix() {
        // Arrange
        Fib fib = new Fib(6);

        // Act
        List<Integer> result = fib.getFibSeries();

        // Assert
        assertEquals(6, result.size());
    }

    @Test
    void shouldNotContainFourWhenRangeIsSix() {
        // Arrange
        Fib fib = new Fib(6);

        // Act
        List<Integer> result = fib.getFibSeries();

        // Assert
        assertFalse(result.contains(4));
    }

    @Test
    void shouldReturnCorrectSequenceWhenRangeIsSix() {
        // Arrange
        Fib fib = new Fib(6);

        // Act
        List<Integer> result = fib.getFibSeries();

        // Assert
        assertEquals(List.of(0, 1, 1, 2, 3, 5), result);
    }

    @Test
    void shouldBeSortedAscendingWhenRangeIsSix() {
        // Arrange
        Fib fib = new Fib(6);

        // Act
        List<Integer> result = fib.getFibSeries();

        // Assert
        for (int i = 0; i < result.size() - 1; i++) {
            assertTrue(result.get(i) <= result.get(i + 1));
        }
    }
}
