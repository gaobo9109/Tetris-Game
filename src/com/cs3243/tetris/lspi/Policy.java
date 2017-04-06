package com.cs3243.tetris.lspi;

import com.cs3243.tetris.NextState;
import com.cs3243.tetris.PlayerSkeleton;
import com.cs3243.tetris.State;
import com.cs3243.tetris.features.Feature;
import com.cs3243.tetris.heuristics.Heuristic;

public class Policy {
	Feature[] features;

	Policy(Feature[] fs) {
		features = fs;
	}

	public int[] getAction(State state) {
		int[][] legalMoves = state.legalMoves();
		Heuristic heuristic = new Heuristic();
		heuristic.setFeatures(features);

		int legalIndex = (new PlayerSkeleton())
				.pickMove(state, legalMoves, new NextState(), heuristic);
		
		return legalMoves[legalIndex];
	}
}
