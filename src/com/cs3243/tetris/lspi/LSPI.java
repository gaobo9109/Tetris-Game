package com.cs3243.tetris.lspi;

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
			int policyAction = policy.getAction(sample.state);

			for (int j = 0; j < policy.features.length; j++) {
				LSPIFeature feature = policy.features[j];

				samplesArray[i][j] = feature.getValue(sample.state, sample.action);
				policyArray[i][j] = feature.getValue(sample.state, policyAction);
			}
			
			rewardsArray[i][0] = sample.getReward();
		}
		
		Matrix samplesMatrix = new Matrix(samplesArray);
		Matrix policyMatrix = new Matrix(policyArray);
		Matrix rewardsMatrix = new Matrix(rewardsArray);
		
		Matrix LHSMatrix = samplesMatrix.transpose().times(samplesMatrix.minus(policyMatrix));
		Matrix RHSMatrix = samplesMatrix.transpose().times(rewardsMatrix);
		
		Matrix weightsMatrix = LHSMatrix.solve(RHSMatrix);
		
		for (int i = 0; i < policy.features.length; i++) {
			policy.weights[i] = weightsMatrix.get(i, 0);
		}
	}
}
