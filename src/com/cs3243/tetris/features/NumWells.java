package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

/**
 * This heuristic calculates the number of wells. A well is defined as a column
 * that is lower than its adjacent columns.
 */
public class NumWells extends Feature {
	@Override
	public double getScore(NextState s) {
		int[] top = s.getTop();

		int count = 0;
		for (int i = 0; i < top.length; i++) {
			if (i == 0 && top[i + 1] > top[i]) {
				count++;
			} else if (i == top.length - 1 && top[i - 1] > top[i]) {
				count++;
			} else if (i != 0 && i != top.length - 1 && top[i - 1] > top[i] && top[i + 1] > top[i]) {
				count++;
			}
		}

		return featureWeight * count;
	}
}
