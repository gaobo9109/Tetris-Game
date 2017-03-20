package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

/**
 * This heuristic calculates the total height difference between adjacent
 * columns.
 */
public class TotalColHeightDiff extends Feature {
    private int sum = 0;

    @Override
    public double getScore() {
        return featureWeight * sum;
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        int[] top = s.getTop();
        if (col != top.length - 1) {
            sum += Math.abs(top[col + 1] - top[col]);
        }
        
    }

    @Override
    public void resetScore() {
        sum = 0;
        
    }
}
