package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

/**
 * This heuristic calculates the number of columns with holes.
 */
public class ColWithHole extends Feature {
    private int count = 0;
    private boolean[] hasHole;
    
    public ColWithHole() {
        hasHole = null;
    }
    
    public ColWithHole(int numCols) {
        hasHole = new boolean[numCols];
    }

    @Override
    public double getScore() {
        return featureWeight * count;
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        int[][] field = s.getField();
        if (!hasHole[col]) {
            if (field[row][col] == 0) {
                hasHole[col] = true;
                count++;
            }
        }        
    }

    @Override
    public void resetScore() {
        count = 0;
    }
}
