package com.cs3243.tetris.features;

import java.util.Random;

import com.cs3243.tetris.NextState;

public abstract class Feature {
	protected double featureWeight;
	private Random rand = new Random();

	/**
	 * Initialize feature with random weight
	 */
	public Feature() {
		featureWeight = 2 * rand.nextDouble() - 100;
	}

	/**
	 * Get feature weight
	 * 
	 * @param weight
	 * @return
	 */
	public double getFeatureWeight() {
		return featureWeight;
	}

	/**
	 * Set feature weight
	 * 
	 * @param weight
	 */
	public void setFeatureWeight(double weight) {
		featureWeight = weight;
	}

	/**
	 * Mutate weight
	 * 
	 * @param mutationProb
	 * @param perturbationRange
	 */
	public void mutate(double mutationProb, double mean, double std) {
		boolean mutate = rand.nextDouble() < mutationProb;
		if (mutate) {
			featureWeight *= rand.nextGaussian() * std + mean;
		}
	}

	/**
	 * Calculate score of feature. Should be overridden with proper
	 * implementation of scoring
	 * 
	 * @param s
	 * @return
	 */
	public abstract double getScore(NextState s);
}
