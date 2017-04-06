package com.cs3243.tetris.lspi;

import com.cs3243.tetris.State;

public class LSPIFeature {
	public State state;
	public int action; // TODO
	
	LSPIFeature(State s, int a) {
		state = s;
		action = a;
	}

	public double getValue(State state, int action) {
		return 0;
	}
}
