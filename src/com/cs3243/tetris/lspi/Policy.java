package com.cs3243.tetris.lspi;

import com.cs3243.tetris.NextState;
import com.cs3243.tetris.PlayerSkeleton;
import com.cs3243.tetris.State;
import com.cs3243.tetris.features.Feature;
import com.cs3243.tetris.heuristics.Heuristic;

public class Policy {
	Heuristic heuristic;

	Policy(Heuristic heuristic) {
		this.heuristic = heuristic;
	}

	public int[] getAction(State state) {
		int[][] legalMoves = state.legalMoves();

		int legalIndex = (new PlayerSkeleton())
				.pickMove(state, legalMoves, new NextState(), heuristic);
		
		return legalMoves[legalIndex];
	}
	
	public double[] getFeatureScores(NextState s) {
		return heuristic.getFeatureScores(s);
	}
	
}
