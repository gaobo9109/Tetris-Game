package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

public class WeightedBlock extends Feature {
    private int sum = 0;
    /*
	@Override
	public double getScore(NextState s) {
		int[][] field = s.getField();
		int cols = field[0].length;
		int rows = field.length;
		
		int sum = 0;
		for(int i=0; i<rows; i++){
			for(int j=0; j<cols; j++){
				//cell in nth row counts n time as much as cell in 1 row
				if(field[i][j] != 0) sum += i+1;
			}
		}
		return sum;
		
	}
	*/
    //NOT USING FEATURE WEIGHT EARLIER?
    @Override
    public double getScore() {
        return featureWeight * sum;
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void resetScore() {
        sum = 0;
    }
}
