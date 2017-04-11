package com.cs3243.tetris.lspi;

import java.util.Arrays;
import java.util.Random;

import com.cs3243.tetris.NextState;
import com.cs3243.tetris.PlayerSkeleton;
import com.cs3243.tetris.State;
import com.cs3243.tetris.features.Feature;
import com.cs3243.tetris.heuristics.Heuristic;

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

		Heuristic heuristic = new Heuristic();
		heuristic.features[0].setFeatureWeight(-93.02173123251356);
		heuristic.features[1].setFeatureWeight(-308.24909375997737);
		heuristic.features[2].setFeatureWeight(-569.9966890295316);
		heuristic.features[3].setFeatureWeight(-0.5566053775002548);
		heuristic.features[4].setFeatureWeight(459.7334110849129);
		heuristic.features[5].setFeatureWeight(-80.920643335677);
		heuristic.features[6].setFeatureWeight(-25.073988080939912);
		heuristic.features[7].setFeatureWeight(-562.3724669171518);
		heuristic.features[8].setFeatureWeight(-691.1628729179465);
		heuristic.features[9].setFeatureWeight(-2691.7035043803835);
		heuristic.features[10].setFeatureWeight(80.98441874131163);
		heuristic.features[11].setFeatureWeight(-723.3118170031345);
		heuristic.features[12].setFeatureWeight(-8.869080880327157);
		
		policy = new Policy(heuristic);

		double[][] samplesArray = new double[samples.length][];

		for (int i = 0; i < samples.length; i++) {
			Sample sample = samples[i];
			NextState nsSample = new NextState();
			nsSample.copyState(sample.state);

			samplesArray[i] = policy.getFeatureScores(nsSample);
		}
		
		samplesMatrix = new Matrix(samplesArray);
	}
	
	public void nextIteration() {
		double[][] policyArray = new double[samples.length][];
		double[][] rewardsArray = new double[samples.length][1];

		for (int i = 0; i < samples.length; i++) {
			Sample sample = samples[i];
			
			// Create a new state fron nextState
			
			NextState nsPolicy = new NextState();
			
			int[] policyAction = policy.getAction(sample.nextState);
			nsPolicy.generateNextState(sample.nextState, policyAction);

			policyArray[i] = policy.getFeatureScores(nsPolicy);

			rewardsArray[i][0] = sample.reward;
		}

		Matrix policyMatrix = new Matrix(policyArray);
		Matrix rewardsMatrix = new Matrix(rewardsArray);
		
		Matrix LHSMatrix = samplesMatrix.transpose().times(samplesMatrix.minus(policyMatrix));
		Matrix RHSMatrix = samplesMatrix.transpose().times(rewardsMatrix);
		
		Matrix weightsMatrix = LHSMatrix.solve(RHSMatrix);

		int j = 0;
		for (int i = 0; i < policy.heuristic.features.length; i++) {
			if (i == 0 || i == 8 || i == 9 || i == 10) {
				continue;
			}
			policy.heuristic.features[i].setFeatureWeight(weightsMatrix.get(j, 0));
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
	}
	
	public static void main(String[] args) {
		LSPI lspi = new LSPI(100000);
		
		for (int i = 0; i < 100; i++) {
			Feature[] features = lspi.policy.heuristic.features;
			
			for (int j = 0; j < features.length; j++) {
				if (j == 0 || j == 8 || j == 9 || j == 10) {
					continue;
				}
				System.out.println(features[j].getFeatureWeight());
			}

			double results = (new PlayerSkeleton()).playFullGame(lspi.policy.heuristic, false);
			System.out.println("Generation " + i + " cleared " + results + " rows.");
			System.out.println("---");

			lspi.nextIteration();
		}
	}
}
