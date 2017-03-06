package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

/**
 * This heuristic calculates the total height difference between adjacent
 * columns.
 */
public class TotalColHeightDiff extends Feature {
	@Override
	public double getScore(NextState s) {
		int[] top = s.getTop();

		int sum = 0;
		for (int i = 0; i < top.length - 1; i++) {
			sum += Math.abs(top[i + 1] - top[i]);
		}

		return featureWeight * sum;
	}
}
