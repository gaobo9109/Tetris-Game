package com.cs3243.tetris.heuristics;

import java.util.Arrays;
import java.util.Random;

import com.cs3243.tetris.features.Feature;

public class PSOHeuristic extends Heuristic {
	private final double BOUND_HIGH = -1;
	private final double BOUND_LOW = 1;
	private final double BOUND_RANGE = BOUND_HIGH - BOUND_LOW;
	
	private double[] personalBestWeights;
	private double personalBestFitness;
	private double[] vel = new double[numFeatures];
	private Random rand = new Random();
	
	public PSOHeuristic() {
		super();
		personalBestWeights = getWeights(this.features);
		personalBestFitness = 0;
		initVels();
	}

	public PSOHeuristic(Heuristic heuristic) {
		this.features = heuristic.features;
		this.fitness = heuristic.fitness;
		
		personalBestWeights = getWeights(this.features);
		personalBestFitness = this.fitness;
		initVels();
	}
	
	private double[] getWeights(Feature[] features) {
		double[] weights = Arrays.stream(features).mapToDouble(feature -> feature.getFeatureWeight()).toArray();
		return weights;
	}

	/**
	 * Initialize velocity: vi ~ U(-BOUND_RANGE, BOUND_RANGE)
	 */
	private void initVels() {
		for (int i = 0; i < numFeatures; i++) {	
			vel[i] = rand.nextDouble() * BOUND_RANGE * 2 - BOUND_RANGE;
		}
	}
	
	/**
	 * Update position of particle by adding its velocity
	 */
	public void updatePos() {
		for (int i = 0; i < numFeatures; i++) {
			features[i].setFeatureWeight(features[i].getFeatureWeight() + vel[i]);
		}
	}
	
	/**
	 * Update a particles velocity taking into account its personal best and the global best
	 */
	public void updateVel(double omega, double rhop, double rhog, Heuristic globalBest) {
		Feature[] globalBestFeatures = globalBest.features;
		double rp, rg;
		
		for (int i = 0; i < numFeatures; i++) {
			rp = rand.nextDouble();
			rg = rand.nextDouble();
			
			vel[i] = 
				omega * vel[i] + 
				rhop * rp * (personalBestWeights[i] - features[i].getFeatureWeight()) + 
				rhog * rg * (globalBestFeatures[i].getFeatureWeight() - features[i].getFeatureWeight());
		}
	}
	
	@Override
	public void setFitness(double fitness) {
		super.setFitness(fitness);
		
		if (fitness > this.personalBestFitness) {
			this.personalBestFitness = fitness;
			this.personalBestWeights = getWeights(this.features);
		}
	}
	
	public double[] getPersonalBestWeights() {
		return this.personalBestWeights;
	}
}
