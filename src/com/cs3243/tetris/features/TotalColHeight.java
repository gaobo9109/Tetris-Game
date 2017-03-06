package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

/**
 * This heuristic calculates the total height of all columns.
 */
public class TotalColHeight extends Feature {
	@Override
	public double getScore(NextState s) {
		int[] top = s.getTop();

		int sum = 0;
		for (int i : top) {
			sum += i;
		}

		return featureWeight * sum;
	}
}
