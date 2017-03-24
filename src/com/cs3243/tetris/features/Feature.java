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
		featureWeight = 2 * 100 * rand.nextDouble() - 100;
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
	
	public Feature clone() {
		try {
			Feature cloneFeature = this.getClass().newInstance();
			cloneFeature.setFeatureWeight(this.featureWeight);
			return cloneFeature;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Calculate score of feature. Should be overridden with proper
	 * implementation of scoring 
	 * @param s
	 * @return double score
	 */
	public abstract double getScore();
	
	/**
     * Updates score of feature. Should be overridden with proper
     * implementation of scoring 
     * @param s, row, col
     */
	public abstract void updateScore(NextState s, int row, int col);
	
	/**
     * Resets. Should be overridden with proper
     * implementation of scoring
     */
	public abstract void resetScore();
}
