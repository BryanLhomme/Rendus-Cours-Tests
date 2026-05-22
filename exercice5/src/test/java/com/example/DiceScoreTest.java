package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de DiceScore")
public class DiceScoreTest {

    @Mock
    private Ide de;

    private DiceScore diceScore;

    @BeforeEach
    void setUp() {
        diceScore = new DiceScore(de);
    }

    @Test
    @DisplayName("Doit retourner valeur * 2 + 10 quand les 2 dés sont identiques (non 6)")
    void shouldReturnDoubledValuePlus10WhenBothDiceAreEqual() {
        when(de.getRoll()).thenReturn(3);

        int result = diceScore.getScore();

        assertEquals(16, result); // 3 * 2 + 10 = 16
        verify(de, times(2)).getRoll();
    }

    @Test
    @DisplayName("Doit retourner 30 quand les 2 dés sont égaux à 6")
    void shouldReturn30WhenBothDiceAreEqualTo6() {
        when(de.getRoll()).thenReturn(6);

        int result = diceScore.getScore();

        assertEquals(30, result);
        verify(de, times(2)).getRoll();
    }

    @Test
    @DisplayName("Doit retourner la valeur la plus haute quand les dés sont différents (premier > second)")
    void shouldReturnHighestValueWhenFirstDieIsGreater() {
        when(de.getRoll()).thenReturn(5, 2);

        int result = diceScore.getScore();

        assertEquals(5, result);
        verify(de, times(2)).getRoll();
    }

    @Test
    @DisplayName("Doit retourner la valeur la plus haute quand les dés sont différents (second > premier)")
    void shouldReturnHighestValueWhenSecondDieIsGreater() {
        when(de.getRoll()).thenReturn(1, 4);

        int result = diceScore.getScore();

        assertEquals(4, result);
        verify(de, times(2)).getRoll();
    }
}
