package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

public class HighestCol extends Feature {

	@Override
	public double getScore(NextState s) {
		int[] top = s.getTop();

		int maxCol = 0;
		for (int colHeight : top) {
			maxCol = Math.max(maxCol, colHeight);
		}

		return featureWeight * maxCol;
	}
}
