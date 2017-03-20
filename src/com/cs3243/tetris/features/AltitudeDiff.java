package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

public class AltitudeDiff extends Feature {
    
    private int minHeight = Integer.MAX_VALUE;
    private int maxHeight = Integer.MIN_VALUE;
	
    @Override
    public double getScore() {
        return featureWeight * (maxHeight - minHeight);
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        int[] top = s.getTop();
        if(top[col] > maxHeight) maxHeight = top[col];
        else if(top[col] < minHeight) minHeight = top[col];
        
    }

    @Override
    public void resetScore() {
        minHeight = Integer.MAX_VALUE;
        maxHeight = Integer.MIN_VALUE;      
    }
}
