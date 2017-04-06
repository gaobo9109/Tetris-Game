package com.cs3243.tetris.lspi;

import com.cs3243.tetris.State;

public class Sample {
	State state;
	int action; // TODO
	
	Sample(State s, int a) {
		state = s;
		action = a;
	}
	
	// TODO
	public State getResultState() {
		return state;
	}
	
	// TODO
	public int getReward() {
		return 0;
	}
}
