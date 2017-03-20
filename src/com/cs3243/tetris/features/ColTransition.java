package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

/*
 * Counting unoccupied -> occupied and occupied -> unoccupied transition within a column
 */

public class ColTransition extends Feature {
    
    private int count = 0;
	
    /*
	@Override
	public double getScore(NextState s) {

		int count = 0;
		int cols = field[0].length;
				
		for(int j=0; j<cols; j++){
			int row = top[j];
			for(int i=0; i<row-1; i++){
				//bottom is considered occupied
				
			}
		}
		return count;
	}
	*/

    @Override
    //EARLIER WAS NOT USING FEATURE WEIGHT?
    public double getScore() {
        return featureWeight * count;
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        int[][] field = s.getField();
        int[] top = s.getTop();
        if(row == 0 && field[row][col] == 0) count ++;
        if(field[row][col] == 0 && field[row+1][col] != 0) count ++;
        else if(field[row][col] != 0 && field[row+1][col] == 0) count ++;
        
    }

    @Override
    public void resetScore() {
        count = 0;        
    }

}
