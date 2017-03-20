package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

/**
 * This heuristic calculates the number of wells. A well is defined as a column
 * that is lower than its adjacent columns.
 */
public class NumWells extends Feature {
    private int count = 0;
    /*
	@Override
	public double getScore(NextState s) {
		int[] top = s.getTop();

		int count = 0;
		for (int i = 0; i < top.length; i++) {

		}

		return featureWeight * count;
	}
	*/

    @Override
    public double getScore() {
        return featureWeight * count;
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        int[] top = s.getTop();
        if (col == 0 && top[col + 1] > top[col]) {
            count++;
        } else if (col == top.length - 1 && top[col - 1] > top[col]) {
            count++;
        } else if (col != 0 && col != top.length - 1 && top[col - 1] > top[col] && top[col + 1] > top[col]) {
            count++;
        }
        
    }

    @Override
    public void resetScore() {
        count = 0;
        
    }
}
