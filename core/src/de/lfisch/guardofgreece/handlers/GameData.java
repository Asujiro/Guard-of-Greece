package de.lfisch.guardofgreece.handlers;

import java.io.Serializable;

import static java.lang.String.valueOf;

public class GameData implements Serializable {

    private static final long serialVersionUID = 1;

    private final int MAX_SCORES = 10;
    private long[] highScores;
    private String[] names;

    private long tentativeScore;

    public GameData() {
        highScores = new long[MAX_SCORES];
        names = new String[MAX_SCORES];
    }

    /**
     * sets up an empty high scores table
     */
    public void init() {
        for(int i = 0; i < MAX_SCORES; i++) {
            highScores[i] = i;
            names[i] = valueOf(i);
        }
    }

    public long[] getHighScores() { return highScores; }
    public String[] getNames() { return names; }

    public long getTentativeScore() { return tentativeScore; }
    public void setTenativeScore(long i) { tentativeScore = i; }

    /**
     * Check if the new Score is higher than the one on the last index of the array
     */
    public boolean isHighScore(long score) {
        return score > highScores[MAX_SCORES - 1];
    }

    /**
     * Set the new HigScore and calls sortHighScores to play it correctly
     */
    public void addHighScore(long newScore, String name) {
        if(isHighScore(newScore)) {
            highScores[MAX_SCORES - 1] = newScore;
            names[MAX_SCORES - 1] = name;
            sortHighScores();
        }
    }


    /**
     * Sort the HighScore Arrays
     */
    public void sortHighScores() {
        for(int i = 0; i < MAX_SCORES; i++) {
            long score = highScores[i];
            String name = names[i];
            int j;
            for(j = i - 1;
                j >= 0 && highScores[j] < score;
                j--) {
                highScores[j + 1] = highScores[j];
                names[j + 1] = names[j];
            }
            highScores[j + 1] = score;
            names[j + 1] = name;
        }
    }

}

















