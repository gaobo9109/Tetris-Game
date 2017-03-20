package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

/**
 * This heuristic calculates the number of rows cleared.
 */
public class RowsCleared extends Feature {
    private double rowsCleared = 0;

    @Override
    public double getScore() {
        // TODO Auto-generated method stub
        return featureWeight * rowsCleared;
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        rowsCleared = featureWeight * s.getRowsCleared();
        
    }

    @Override
    public void resetScore() {
       rowsCleared = 0;
        
    }
}
