package com.cs3243.tetris.features;

import javax.swing.text.TabStop;

import com.cs3243.tetris.NextState;

/**
 * This heuristic calculates the total height of all columns.
 */
public class TotalColHeight extends Feature {
    
    private int sum = 0;
    /*
	@Override
	public double getScore(NextState s) {

		int sum = 0;
		for (int i : top) {
			sum += i;
		}

		return featureWeight * sum;
	}
	*/

    @Override
    public double getScore() {
        // TODO Auto-generated method stub
        return featureWeight * sum;
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        int[] top = s.getTop();
        sum += top[col];
        
    }

    @Override
    public void resetScore() {
        sum = 0;
        
    }
}
