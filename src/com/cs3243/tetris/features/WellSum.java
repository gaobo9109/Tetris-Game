package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

public class WellSum extends Feature{
	private int wellSum = 0;
	
    @Override
    public double getScore() {
        return featureWeight * wellSum ;
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        int[] top = s.getTop();

        if (col == 0 || col == top.length - 1) {
            if(col == 0 && top[col + 1] > top[col]){
                wellSum += top[col+1] - top[col];
            } else if(col == top.length -1 && top[col-1] > top[col]){
                wellSum += top[col-1] - top[col];
            }
        } else if (top[col - 1] > top[col] && top[col + 1] > top[col]) {
            wellSum += Math.min(top[col-1], top[col+1]) - top[col]; 
        }
        
    }

    @Override
    public void resetScore() {
        wellSum = 0;
        
    }

}
