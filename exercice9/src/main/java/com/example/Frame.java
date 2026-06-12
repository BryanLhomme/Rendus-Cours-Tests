package com.example;

import java.util.ArrayList;
import java.util.List;

public class Frame {
    private int score;
    private boolean lastFrame;
    private IGenerateur generateur;
    private List<Roll> rolls;

    public Frame(IGenerateur generateur, boolean lastFrame) {
        this.lastFrame = lastFrame;
        this.generateur = generateur;
        this.rolls = new ArrayList<>();
        this.score = 0;
    }

    public boolean makeRoll() {
        int rollCount = rolls.size();

        if (!lastFrame) {
            if (rollCount == 0) {
                int pins = generateur.randomPin(10);
                rolls.add(new Roll(pins));
                score += pins;
                return true;
            }
            if (rollCount == 1) {
                if (rolls.get(0).getPins() == 10) {
                    return false;
                }
                int pins = generateur.randomPin(10 - rolls.get(0).getPins());
                rolls.add(new Roll(pins));
                score += pins;
                return true;
            }
            return false;
        }

        // Série finale
        if (rollCount >= 3) {
            return false;
        }

        if (rollCount == 2) {
            int first = rolls.get(0).getPins();
            int second = rolls.get(1).getPins();
            boolean isStrike = first == 10;
            boolean isSpare = (first + second == 10);
            if (!isStrike && !isSpare) {
                return false;
            }
        }

        int pins = generateur.randomPin(10);
        rolls.add(new Roll(pins));
        score += pins;
        return true;
    }

    public int getScore() {
        return score;
    }
}
