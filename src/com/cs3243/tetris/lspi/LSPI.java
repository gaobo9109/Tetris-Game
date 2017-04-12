package com.cs3243.tetris.lspi;

import java.util.Arrays;
import java.util.Random;

import com.cs3243.tetris.NextState;
import com.cs3243.tetris.PlayerSkeleton;
import com.cs3243.tetris.State;
import com.cs3243.tetris.features.Feature;
import com.cs3243.tetris.heuristics.Heuristic;
import com.cs3243.tetris.lspi.features.LSPIFeature;

public class LSPI {
	public Sample[] samples;
	public Policy policy;
	public Matrix samplesMatrix;
	
	LSPI(Sample[] s, Policy p) {
		samples = s;
		policy = p;
	}

	LSPI(int numberOfRandomSamples) {
		generateSamples(numberOfRandomSamples);
		
		policy = new Policy();

		double[][] samplesArray = new double[samples.length][];

		for (int i = 0; i < samples.length; i++) {
			Sample sample = samples[i];
			NextState nsSample = new NextState();
			nsSample.copyState(sample.state);

			samplesArray[i] = policy.getFeatureScores(nsSample, sample.action);
		}
		
		samplesMatrix = new Matrix(samplesArray);
	}
	
	public void nextIteration() {
		double[][] policyArray = new double[samples.length][];
		double[][] rewardsArray = new double[samples.length][1];

		for (int i = 0; i < samples.length; i++) {
			Sample sample = samples[i];
			
			NextState nsPolicy = new NextState();
			
			int[] policyAction = policy.getAction(sample.nextState);
			nsPolicy.generateNextState(sample.nextState, policyAction);

			policyArray[i] = policy.getFeatureScores(nsPolicy, policyAction);

			rewardsArray[i][0] = sample.reward;
		}

		Matrix policyMatrix = new Matrix(policyArray);
		Matrix rewardsMatrix = new Matrix(rewardsArray);
		
		Matrix LHSMatrix = samplesMatrix.transpose().times(samplesMatrix.minus(policyMatrix));
		Matrix RHSMatrix = samplesMatrix.transpose().times(rewardsMatrix);
		
		Matrix weightsMatrix = LHSMatrix.solve(RHSMatrix);

		int j = 0;
		for (int i = 0; i < policy.features.length; i++) {
			policy.features[i].weight = weightsMatrix.get(j, 0);
			j++;
		}
	}
	
	public void generateSamples(int count) {
		samples = new Sample[count];
		Random random = new Random();

		for (int i = 0; i < count; i++) {
			NextState state = NextState.generateRandomState();
			int[][] moves = state.legalMoves();
			samples[i] = new Sample(state, moves[random.nextInt(moves.length)]);
		}
		
		// Inject first sample
		Sample first = new Sample(NextState.generateStateWithOneHole(), new int[]{0, 0});
		samples[0] = first;
	}
	
	public static void main(String[] args) {
		LSPI lspi = new LSPI(150000);
		
		double oldRMS = 0;
		double rms = 0;
		for (int i = 0; i < 100; i++) {
			LSPIFeature[] features = lspi.policy.features;
			
			oldRMS = rms;
			rms = 0;
			
			for (int j = 0; j < features.length; j++) {
				rms = Math.pow(features[j].weight, 2);
				// System.out.println(features[j].getFeatureWeight());
			}
			
			rms = Math.sqrt(rms);
			System.out.println("Difference: " + (Math.abs(rms - oldRMS)));

			if (i != 0) {
				double results = (new PlayerSkeleton()).playFullLSPIGame(lspi.policy, false);
				System.out.println("Generation " + i + " cleared " + results + " rows.");
			}

			lspi.nextIteration();
		}
	}
}
