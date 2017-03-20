package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

/**
 * This heuristic calculates the number of columns with holes.
 */
public class ColWithHole extends Feature {
    private int count = 0;
    /*
	@Override
	public double getScore(NextState s) {
		int[][] field = s.getField();
		int[] top = s.getTop();

		int count = 0;
		for (int i = 0; i < top.length; i++) {
			int colHeight = top[i];
			for (int j = 0; j < colHeight - 1; j++) {
				if (field[j][i] == 0) {
					count++;
					break;
				}
			}
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
        int[][] field = s.getField();
        int[] top = s.getTop();
        
    }

    @Override
    public void resetScore() {
        count = 0;
        
    }
}
