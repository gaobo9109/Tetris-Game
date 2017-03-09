package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

/*
 * Counting unoccupied -> occupied and occupied -> unoccupied transition within a column
 */

public class ColTransition extends Feature{
	
	@Override
	public double getScore(NextState s) {
		int[][] field = s.getField();
		int[] top = s.getTop();
		int count = 0;
		int cols = field[0].length;
				
		for(int j=0; j<cols; j++){
			int row = top[j];
			for(int i=0; i<row-1; i++){
				//bottom is considered occupied
				if(i == 0 && field[i][j] == 0) count ++;
				if(field[i][j] == 0 && field[i+1][j] != 0) count ++;
				else if(field[i][j] != 0 && field[i+1][j] == 0) count ++;
			}
		}
		return count;
	}

}
