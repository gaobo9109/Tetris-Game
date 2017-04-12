package com.cs3243.tetris.lspi.features;

import com.cs3243.tetris.NextState;

public class ErodedPieces extends LSPIFeature {

	@Override
	public double getScore(NextState state, int[] action) {
		NextState next = new NextState();
		next.generateNextState(state, action);
		return next.getRowsCleared() - state.getRowsCleared();
	}

}
