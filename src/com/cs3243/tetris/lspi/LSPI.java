package com.cs3243.tetris.lspi;

import com.cs3243.tetris.NextState;
import com.cs3243.tetris.features.Feature;

public class LSPI {
	public Sample[] samples;
	public Policy policy;
	
	LSPI(Sample[] s, Policy p) {
		samples = s;
		policy = p;
	}
	
	public void nextIteration() {
		double[][] samplesArray = new double[samples.length][policy.features.length];
		double[][] policyArray = new double[samples.length][policy.features.length];
		double[][] rewardsArray = new double[samples.length][1];

		for (int i = 0; i < samples.length; i++) {
			Sample sample = samples[i];
			int[] policyAction = policy.getAction(sample.state);

			for (int j = 0; j < policy.features.length; j++) {
				Feature feature = policy.features[j];

				NextState nsSample = sample.nextState;
				
				// TODO: this should be the score of feature acting on nsSample
				samplesArray[i][j] = feature.getScore();
				
				NextState nsPolicy = new NextState();
				nsPolicy.generateNextState(sample.state, policyAction);
				
				// TODO: this should be the score of feature acting on nsPolicy
				policyArray[i][j] = feature.getScore();
			}
			
			rewardsArray[i][0] = sample.reward;
		}
		
		Matrix samplesMatrix = new Matrix(samplesArray);
		Matrix policyMatrix = new Matrix(policyArray);
		Matrix rewardsMatrix = new Matrix(rewardsArray);
		
		Matrix LHSMatrix = samplesMatrix.transpose().times(samplesMatrix.minus(policyMatrix));
		Matrix RHSMatrix = samplesMatrix.transpose().times(rewardsMatrix);
		
		Matrix weightsMatrix = LHSMatrix.solve(RHSMatrix);
		
		for (int i = 0; i < policy.features.length; i++) {
			policy.features[i].setFeatureWeight(weightsMatrix.get(i, 0));
		}
	}
}
