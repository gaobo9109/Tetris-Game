package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

/**
 * This heuristic calculates the height of the lowest column.
 */
public class DeepestWell extends Feature {
    
    private int maxWellDepth = 0;
  
    @Override
    public double getScore() {
        return featureWeight * maxWellDepth;
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        int[] top = s.getTop();
        int wellDepth = 0;
        if (col == 0 && top[col + 1] > top[col]) {
            wellDepth = top[col + 1] - top[col];
        } else if (col == top.length - 1 && top[col - 1] > top[col]) {
            wellDepth = top[col - 1] - top[col];
        } else if (col != 0 && col != top.length - 1 && top[col - 1] > top[col] && top[col + 1] > top[col]) {
            wellDepth = Math.min(top[col - 1], top[col + 1]) - top[col]; 
        }

        maxWellDepth = Math.max(maxWellDepth, wellDepth);
    }

    @Override
    public void resetScore() {
       maxWellDepth = 0;
        
    }
}
