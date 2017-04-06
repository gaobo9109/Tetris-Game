package com.cs3243.tetris.lspi;

import com.cs3243.tetris.State;
import com.cs3243.tetris.features.Feature;

public class Policy {
	Feature[] features;
	double[] weights;
	
	Policy(Feature[] fs) {
		features = fs;
		weights = new double[features.length];
	}
	
	// TODO
	public int[] getAction(State state) {
		return new int[0];
	}
}
