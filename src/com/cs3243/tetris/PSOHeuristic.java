package com.cs3243.tetris;

import java.util.Arrays;
import java.util.Random;

import com.cs3243.tetris.features.Feature;

public class PSOHeuristic extends Heuristic {
	private final double BOUND_HIGH = -1;
	private final double BOUND_LOW = 1;
	private final double BOUND_RANGE = BOUND_HIGH - BOUND_LOW;
	
	private Double[] personalBestWeights;
	private double personalBestFitness;
	private Double[] vel;
	private Random rand = new Random();
	
	public PSOHeuristic() {
		super();
		personalBestWeights = getWeights(this.features);
		personalBestFitness = fitness;
		initVels();
	}

	public PSOHeuristic(Heuristic heuristic) {
		personalBestWeights = getWeights(heuristic.features);
		personalBestFitness = heuristic.fitness;
		initVels();
	}
	
	private Double[] getWeights(Feature[] features) {
		return (Double[]) Arrays.stream(features).map(feature -> feature.getFeatureWeight()).toArray();
	}

	private void initVels() {
		for (int i = 0; i < numFeatures; i++) {				
			vel[i] = rand.nextDouble() * BOUND_RANGE * 2 - BOUND_RANGE;
		}
	}
	
	public void updatePos() {
		for (int i = 0; i < numFeatures; i++) {
			features[i].setFeatureWeight(features[i].getFeatureWeight() + vel[i]);
		}
	}
	
	public void updateVel(double omega, double rhop, double rhog, Feature[] globalBest) {
		double rp, rg;
		
		for (int i = 0; i < numFeatures; i++) {
			rp = rand.nextDouble();
			rg = rand.nextDouble();
			
			vel[i] = 
				omega * vel[i] + 
				rhop * rp * (personalBestWeights[i] - features[i].getFeatureWeight()) + 
				rhog * rg * (globalBest[i].getFeatureWeight() - features[i].getFeatureWeight());
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
	
	public Double[] getPersonalBestWeights() {
		return this.personalBestWeights;
	}
}
