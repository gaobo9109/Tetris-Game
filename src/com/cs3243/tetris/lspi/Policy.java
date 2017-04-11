package com.cs3243.tetris.lspi;

import java.util.Arrays;

import com.cs3243.tetris.NextState;
import com.cs3243.tetris.PlayerSkeleton;
import com.cs3243.tetris.State;
import com.cs3243.tetris.heuristics.Heuristic;

public class Policy {
	Heuristic heuristic;

	Policy(Heuristic heuristic) {
		this.heuristic = heuristic;
	}

	public int[] getAction(NextState state) {
		int[][] legalMoves = state.legalMoves();

		int legalIndex = (new PlayerSkeleton())
				.pickMove(state, legalMoves, new NextState(), heuristic);
		
		return legalMoves[legalIndex];
	}
	
	public double[] getFeatureScores(NextState s) {
		double[] featureScores = heuristic.getFeatureScores(s);
		// VERY VERY HACKY CODE: remove heuristics number 0, 8, 9, 10
		double[] result = new double[featureScores.length - 4];
		int j = 0;
		for (int i = 0; i < featureScores.length; i++) {
			if (i == 0 || i == 8 || i == 9 || i == 10) {
				continue;
			}
			result[j] = featureScores[i];
			j++;
		}
		
		return result;
	}
	
}
