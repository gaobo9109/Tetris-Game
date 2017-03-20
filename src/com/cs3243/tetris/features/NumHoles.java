package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

/**
 * This heuristic calculates the number of holes. A column has a hole if it is
 * lower than the highest column.
 */
public class NumHoles extends Feature {
    private int holes = 0;
    /*
	@Override
	public double getScore(NextState s) {
		int[][] field = s.getField();
		int[] top = s.getTop();

		int holes = 0;
		for (int i = 0; i < top.length; i++) {
			int colHeight = top[i];
			for (int j = 0; j < colHeight - 1; j++) {
				// any empty cell beneath the top row cell in that col is a hole
				if (field[j][i] == 0) {
					holes++;
				}
			}
		}

		return featureWeight * holes;
	}
	*/

    @Override
    public double getScore() {
        return featureWeight * holes;
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void resetScore() {
        holes = 0;     
    }
}
