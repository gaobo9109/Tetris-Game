package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

/**
 * This heuristic calculates the height of the lowest column.
 */
public class DeepestWell extends Feature {
	@Override
	public double getScore(NextState s) {
		int[] top = s.getTop();

		int minWellRow = top[0];

		for (int i = 0; i < top.length; i++) {
			int wellRow = 0;

			if (i == 0 && top[i + 1] > top[i]) {
				wellRow = top[i];
			} else if (i == top.length - 1 && top[i - 1] > top[i]) {
				wellRow = top[i];
			} else if (top[i - 1] > top[i] && top[i + 1] > top[i]) {
				wellRow = top[i];
			}

			minWellRow = Math.min(minWellRow, wellRow);
		}

		return featureWeight * minWellRow;
	}
}
