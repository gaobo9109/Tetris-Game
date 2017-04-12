package com.cs3243.tetris.lspi.features;

import com.cs3243.tetris.NextState;

public abstract class LSPIFeature {
	public double weight;
	
	abstract public double getScore(NextState state, int[] action);
	
	public double getWeightedScore(NextState state, int[] action) {
		return weight * getScore(state, action);
	}
}
