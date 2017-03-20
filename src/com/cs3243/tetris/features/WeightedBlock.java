package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

public class WeightedBlock extends Feature {
    private int sum = 0;
    
    @Override
    public double getScore() {
        return featureWeight * sum;
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        int[][] field = s.getField();
        if(field[row][col] != 0) sum += row+1;
        
    }

    @Override
    public void resetScore() {
        sum = 0;
    }
}
