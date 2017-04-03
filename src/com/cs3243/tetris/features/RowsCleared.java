package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;
import com.cs3243.tetris.State;

/**
 * This heuristic calculates the number of rows cleared.
 */
public class RowsCleared extends Feature {
    private double rowsCleared = 0;

    @Override
    public double getScore() {
        return featureWeight * rowsCleared;
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        throw new UnsupportedOperationException(); 
    }
    
    @Override
    public void updateScore(State s, NextState ns, int row, int col) {
    	rowsCleared = ns.getRowsCleared() - s.getRowsCleared();
    };

    @Override
    public void resetScore() {
       rowsCleared = 0;
        
    }
}
