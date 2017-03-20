package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

/**
 * This heuristic calculates the number of holes. A column has a hole if it is
 * lower than the highest column.
 */
public class NumHoles extends Feature {
    private int holes = 0;

    @Override
    public double getScore() {
        return featureWeight * holes;
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        int[][] field = s.getField();
        if (field[row][col] == 0) {
            holes++;
        }
        
    }

    @Override
    public void resetScore() {
        holes = 0;     
    }
}
