package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

public class WellSum extends Feature{
	private int wellSum = 0;
	/*
	@Override
	public double getScore(NextState s) {
		int[] top = s.getTop();

		int wellSum = 0;

		for (int i = 0; i < top.length; i++) {

			if (i == 0 || i == top.length - 1) {
				if(i == 0 && top[i + 1] > top[i]){
					wellSum += top[i+1] - top[i];
				} else if(i == top.length -1 && top[i-1] > top[i]){
					wellSum += top[i-1] - top[i];
				}
			} else if (top[i - 1] > top[i] && top[i + 1] > top[i]) {
				wellSum += Math.min(top[i-1], top[i+1]) - top[i]; 
			}

			
		}
		return wellSum;
	}
	*/
	//NOT USING FEATURE WEIGHT EARLIER?
    @Override
    public double getScore() {
        // TODO Auto-generated method stub
        return featureWeight * wellSum ;
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        // TODO Auto-generated method stub
        //DAFUQ? LOOKS WRONG TO ME
        
    }

    @Override
    public void resetScore() {
        wellSum = 0;
        
    }

}
