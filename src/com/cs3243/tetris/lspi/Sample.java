package com.cs3243.tetris.lspi;

import com.cs3243.tetris.NextState;
import com.cs3243.tetris.State;

public class Sample {
	NextState state;
	int[] action;
	NextState nextState;
	int reward;
	
	Sample(NextState s, int[] a) {
		state = s;
		action = a;
		
		NextState ns = new NextState();
		ns.generateNextState(state, action);
		nextState = ns;
		reward = ns.getRowsCleared() - state.getRowsCleared();
	}
}
