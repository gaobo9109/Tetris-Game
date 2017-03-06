package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

/**
 * This heuristic calculates the number of rows cleared.
 */
public class RowsCleared extends Feature {
	@Override
	public double getScore(NextState s) {
		return featureWeight * s.getRowsCleared();
	}
}
