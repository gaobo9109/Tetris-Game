package com.cs3243.tetris.lspi;

import com.cs3243.tetris.NextState;
import com.cs3243.tetris.State;

public class Sample {
	NextState state;
	int[] action;
	NextState nextState = new NextState();
	int reward;
	
	Sample(NextState s, int[] a) {
		state = s;
		action = a;
		
		nextState.generateNextState(state, action);
		reward = nextState.getRowsCleared() - state.getRowsCleared();
	}
}
