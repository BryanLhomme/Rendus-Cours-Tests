package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de Frame")
public class FrameTest {

    @Mock
    private IGenerateur generateur;

    // --- Série standard ---

    @Test
    @DisplayName("Le premier lancer doit augmenter le score de la série standard")
    void shouldIncreaseScoreWhenFirstRollIsMadeInStandardFrame() {
        when(generateur.randomPin(anyInt())).thenReturn(5);
        Frame frame = new Frame(generateur, false);

        frame.makeRoll();

        assertEquals(5, frame.getScore());
    }

    @Test
    @DisplayName("Le second lancer doit augmenter le score de la série standard")
    void shouldIncreaseScoreWhenSecondRollIsMadeInStandardFrame() {
        when(generateur.randomPin(anyInt())).thenReturn(3, 4);
        Frame frame = new Frame(generateur, false);

        frame.makeRoll();
        frame.makeRoll();

        assertEquals(7, frame.getScore());
    }

    @Test
    @DisplayName("Un strike empêche un second lancer dans une série standard")
    void shouldRejectSecondRollWhenStandardFrameStartsWithStrike() {
        when(generateur.randomPin(anyInt())).thenReturn(10);
        Frame frame = new Frame(generateur, false);

        frame.makeRoll();
        boolean result = frame.makeRoll();

        assertFalse(result);
    }

    @Test
    @DisplayName("Il ne doit pas être possible de lancer plus de 2 fois dans une série standard")
    void shouldRejectThirdRollWhenStandardFrameAlreadyHasTwoRolls() {
        when(generateur.randomPin(anyInt())).thenReturn(3, 4);
        Frame frame = new Frame(generateur, false);

        frame.makeRoll();
        frame.makeRoll();
        boolean result = frame.makeRoll();

        assertFalse(result);
    }

    // --- Série finale ---

    @Test
    @DisplayName("Un strike en série finale autorise un second lancer")
    void shouldAcceptSecondRollWhenLastFrameStartsWithStrike() {
        when(generateur.randomPin(anyInt())).thenReturn(10, 5);
        Frame frame = new Frame(generateur, true);

        frame.makeRoll();
        boolean result = frame.makeRoll();

        assertTrue(result);
    }

    @Test
    @DisplayName("Le score augmente lors du second lancer après un strike en série finale")
    void shouldIncreaseScoreWhenSecondRollIsMadeAfterStrikeInLastFrame() {
        when(generateur.randomPin(anyInt())).thenReturn(10, 5);
        Frame frame = new Frame(generateur, true);

        frame.makeRoll();
        frame.makeRoll();

        assertEquals(15, frame.getScore());
    }

    @Test
    @DisplayName("Un strike en série finale autorise un troisième lancer")
    void shouldAcceptThirdRollWhenLastFrameStartsWithStrike() {
        when(generateur.randomPin(anyInt())).thenReturn(10, 7, 2);
        Frame frame = new Frame(generateur, true);

        frame.makeRoll();
        frame.makeRoll();
        boolean result = frame.makeRoll();

        assertTrue(result);
    }

    @Test
    @DisplayName("Le score augmente lors du troisième lancer après un strike en série finale")
    void shouldIncreaseScoreWhenThirdRollIsMadeAfterStrikeInLastFrame() {
        when(generateur.randomPin(anyInt())).thenReturn(10, 5, 3);
        Frame frame = new Frame(generateur, true);

        frame.makeRoll();
        frame.makeRoll();
        frame.makeRoll();

        assertEquals(18, frame.getScore());
    }

    @Test
    @DisplayName("Un spare en série finale autorise un troisième lancer")
    void shouldAcceptThirdRollWhenLastFrameStartsWithSpare() {
        when(generateur.randomPin(anyInt())).thenReturn(7, 3, 5);
        Frame frame = new Frame(generateur, true);

        frame.makeRoll();
        frame.makeRoll();
        boolean result = frame.makeRoll();

        assertTrue(result);
    }

    @Test
    @DisplayName("Le score augmente lors du troisième lancer après un spare en série finale")
    void shouldIncreaseScoreWhenThirdRollIsMadeAfterSpareInLastFrame() {
        when(generateur.randomPin(anyInt())).thenReturn(7, 3, 5);
        Frame frame = new Frame(generateur, true);

        frame.makeRoll();
        frame.makeRoll();
        frame.makeRoll();

        assertEquals(15, frame.getScore());
    }

    @Test
    @DisplayName("Sans strike ni spare, le troisième lancer est refusé en série finale")
    void shouldRejectThirdRollWhenLastFrameHasNoStrikeOrSpare() {
        when(generateur.randomPin(anyInt())).thenReturn(3, 4);
        Frame frame = new Frame(generateur, true);

        frame.makeRoll();
        frame.makeRoll();
        boolean result = frame.makeRoll();

        assertFalse(result);
    }

    @Test
    @DisplayName("Il ne doit pas être possible de lancer plus de 3 fois en série finale")
    void shouldRejectFourthRollInLastFrame() {
        when(generateur.randomPin(anyInt())).thenReturn(10, 7, 2);
        Frame frame = new Frame(generateur, true);

        frame.makeRoll();
        frame.makeRoll();
        frame.makeRoll();
        boolean result = frame.makeRoll();

        assertFalse(result);
    }
}
