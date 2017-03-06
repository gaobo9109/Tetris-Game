package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

/**
 * This heuristic calculates the number of holes.
 */
public class NumHoles extends Feature {
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
}
