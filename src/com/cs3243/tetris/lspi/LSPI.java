package com.cs3243.tetris.lspi;

import java.util.Arrays;
import java.util.Random;

import com.cs3243.tetris.NextState;
import com.cs3243.tetris.State;
import com.cs3243.tetris.features.Feature;
import com.cs3243.tetris.heuristics.Heuristic;

public class LSPI {
	public Sample[] samples;
	public Policy policy;
	
	LSPI(Sample[] s, Policy p) {
		samples = s;
		policy = p;
	}
	
	LSPI(int numberOfRandomSamples) {
		generateSamples(numberOfRandomSamples);
		
		Random random = new Random();
		Heuristic heuristic = new Heuristic();
		Feature[] features = heuristic.features;
		for (int i = 0; i < features.length; i++) {
			Feature feature = features[i];
			feature.setFeatureWeight(random.nextDouble());
		}
		policy = new Policy(heuristic);
	}
	
	public void nextIteration() {
		double[][] samplesArray = new double[samples.length][];
		double[][] policyArray = new double[samples.length][];
		double[][] rewardsArray = new double[samples.length][1];

		for (int i = 0; i < samples.length; i++) {
			Sample sample = samples[i];
			NextState nsSample = new NextState();
			nsSample.copyState(sample.state);

			int[] policyAction = policy.getAction(sample.state);
			NextState nsPolicy = new NextState();
			nsPolicy.generateNextState(sample.state, policyAction);

			samplesArray[i] = policy.getFeatureScores(nsSample);
			policyArray[i] = policy.getFeatureScores(nsPolicy);

			rewardsArray[i][0] = sample.reward;
		}
		
		Matrix samplesMatrix = new Matrix(samplesArray);
		Matrix policyMatrix = new Matrix(policyArray);
		Matrix rewardsMatrix = new Matrix(rewardsArray);
		
		Matrix LHSMatrix = samplesMatrix.transpose().times(samplesMatrix.minus(policyMatrix));
		Matrix RHSMatrix = samplesMatrix.transpose().times(rewardsMatrix);
		
		Matrix weightsMatrix = LHSMatrix.solve(RHSMatrix);
		
		for (int i = 0; i < policy.heuristic.features.length - 4; i++) {
			policy.heuristic.features[i].setFeatureWeight(weightsMatrix.get(i, 0));
		}
	}
	
	public void generateSamples(int count) {
		samples = new Sample[count];
		Random random = new Random();

		for (int i = 0; i < count; i++) {
			State state = State.generateRandomState();
			int[][] moves = state.legalMoves();
			samples[i] = new Sample(state, moves[random.nextInt(moves.length)]);
		}
	}
	
	public static void main(String[] args) {
		LSPI lspi = new LSPI(100000);
		
		for (int i = 0; i < 100; i++) {
			Feature[] features = lspi.policy.heuristic.features;
			
			for (int j = 0; j < features.length; j++) {
				System.out.println(features[j].getFeatureWeight());
			}
			System.out.println("---");

			lspi.nextIteration();
		}
	}
}
