package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

/*
 * Counting unoccupied -> occupied and occupied -> unoccupied transition within a row
 */

public class RowTransition extends Feature{
	
	@Override
	public double getScore(NextState s) {
		int[][] field = s.getField();
		int count = 0;
		int rows = field.length;
		int cols = field[0].length;
				
		for(int i=0; i<rows; i++){
			for(int j=0; j<cols; j++){				
				// left wall and right wall considered occupied
			    if(j == 0 && field[i][j] == 0) count ++;
				else if(j == cols - 1 && field[i][j] == 0) count ++;
			    
				if(j != cols -1){
					if(field[i][j] == 0 && field[i][j+1] != 0) count ++;
					else if(field[i][j] != 0 && field[i][j+1] == 0) count ++;
				}
			
			}
		}
		return count;
	}

}
