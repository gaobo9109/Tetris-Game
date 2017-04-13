package com.cs3243.tetris.lspi;

import java.util.Arrays;
import java.util.Random;

import com.cs3243.tetris.NextState;
import com.cs3243.tetris.PlayerSkeleton;
import com.cs3243.tetris.State;
import com.cs3243.tetris.heuristics.Heuristic;
import com.cs3243.tetris.lspi.features.ColTransitions;
import com.cs3243.tetris.lspi.features.ErodedPieces;
import com.cs3243.tetris.lspi.features.HeightSum;
import com.cs3243.tetris.lspi.features.CumulativeWells;
import com.cs3243.tetris.lspi.features.CoveredEmptyCells;
import com.cs3243.tetris.lspi.features.LSPIFeature;
import com.cs3243.tetris.lspi.features.LandingHeight;
import com.cs3243.tetris.lspi.features.NumberOfHoles;
import com.cs3243.tetris.lspi.features.RowTransitions;

public class Policy {
	LSPIFeature[] features;

	Policy() {
		features = new LSPIFeature[]{new ColTransitions(), new CoveredEmptyCells(), new CumulativeWells(), new ErodedPieces(),
				new HeightSum(), new LandingHeight(), new NumberOfHoles(), new RowTransitions()};
		
		for (LSPIFeature feature : features) {
			feature.weight = (new Random()).nextDouble();
		}
		
	}

	public int[] getAction(NextState state) {
		int[][] legalMoves = state.legalMoves();
		
		double maxScore = Double.NEGATIVE_INFINITY;
		int actionIndex = -1;
		for (int i = 0; i < legalMoves.length; i++) {
			int[] move = legalMoves[i];
			
			double score = 0;
			for (LSPIFeature feature : features) {
				score += feature.getWeightedScore(state, move);
			}
			
			if (score > maxScore) {
				actionIndex = i;
			}
		}
		
		return legalMoves[actionIndex];
	}
	
	public double[] getFeatureScores(NextState s, int[] action) {
		double[] result = new double[features.length];
		
		for (int i = 0; i < features.length; i++) {
			result[i] = features[i].getWeightedScore(s, action);
		}
		
		return result;
	}
	
}
