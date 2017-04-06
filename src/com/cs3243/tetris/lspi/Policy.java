package com.cs3243.tetris.lspi;

import com.cs3243.tetris.State;

public class Policy {
	LSPIFeature[] features;
	double[] weights;
	
	Policy(LSPIFeature[] f) {
		features = f;
		weights = new double[features.length];
	}
	
	// TODO
	public int getAction(State state) {
		return 0;
	}
}
