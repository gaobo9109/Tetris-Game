package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

/**
 * This heuristic calculates the height of the highest column.
 */
public class HighestCol extends Feature {
    private int maxCol = 0;

    @Override
    public double getScore() {
        return featureWeight * maxCol;
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        int[] top = s.getTop();
        maxCol = Math.max(maxCol, top[col]);
        
    }

    @Override
    public void resetScore() {
        maxCol = 0;
        
    }
}
