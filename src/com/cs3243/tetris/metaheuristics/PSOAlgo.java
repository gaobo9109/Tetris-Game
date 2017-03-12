package com.cs3243.tetris.metaheuristics;

import java.util.ArrayList;

import com.cs3243.tetris.Heuristic;
import com.cs3243.tetris.cluster.Cluster;
import com.cs3243.tetris.features.Feature;

public class PSOAlgo extends Metaheuristic {
	private final double BOUND_HIGH = -1;
	private final double BOUND_LOW = 1;
	private final double BOUND_RANGE = BOUND_HIGH - BOUND_LOW;
	private final double OMEGA = 0.6;
	private final double RHOP = 1.4;
	private final double RHOG = 2.0;
	
	Heuristic[] personalBests;
	Heuristic globalBest;
	double[][] velocities;
	
	int popSize;
	int numFeatures;
	
	@Override
	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
		
		popSize = this.cluster.getPopSize();
		numFeatures = this.cluster.getPopulation().get(0).getFeatures().length;
		
		cluster.evaluateFitness();
		initPositions();
		initVels();
	}
	
	/**
	 * Initialize personal bests with current fitness for each heuristic
	 * Update global best to be best over all the current personal bests 
	 */
	private void initPositions() {
		globalBest.setFitness(0);
		personalBests = new Heuristic[popSize];
		ArrayList<Heuristic> population = this.cluster.getPopulation();
		
		for (int i = 0; i < popSize; i++) {
			personalBests[i] = population.get(i).clone();
			
			if (personalBests[i].getFitness() > globalBest.getFitness()) {
				globalBest = personalBests[i].clone();
			}
		}
	}
	
	/**
	 * Initialize every particle's velocity: vi ~ U(-BOUND_RANGE, BOUND_RANGE)
	 */
	private void initVels() {
		velocities = new double[popSize][numFeatures];
		
		for (int i = 0; i < popSize; i++) {
			for (int j = 0; j < numFeatures; j++) {				
				velocities[i][j] = rand.nextDouble() * BOUND_RANGE * 2 - BOUND_RANGE;
			}
		}
	}
	
	/**
	 * Update position of particle by adding its velocity
	 * @param index of the particle
	 * @param heuristic at the index
	 */
	private void updatePos(int index, Heuristic heuristic) {
		Feature[] features = heuristic.getFeatures();
		
		for (int i = 0; i < numFeatures; i++) {
			features[i].setFeatureWeight(features[i].getFeatureWeight() + velocities[index][i]);
		}
	}
	
	/**
	 * Update a particles velocity taking into account its personal best and the global best
	 * @param index of the particle
	 * @param heuristic at the index
	 */
	private void updateVel(int index, Heuristic heuristic) {
		Feature[] features = heuristic.getFeatures();
		Feature[] personalBestFeatures = personalBests[index].getFeatures();
		Feature[] globalBestFeatures = globalBest.getFeatures();
		double rp, rg;
		
		for (int i = 0; i < numFeatures; i++) {
			rp = rand.nextDouble();
			rg = rand.nextDouble();
			
			velocities[index][i] = 
				OMEGA * velocities[index][i] + 
				RHOP * rp * (personalBestFeatures[i].getFeatureWeight() - features[i].getFeatureWeight()) + 
				RHOG * rg * (globalBestFeatures[i].getFeatureWeight() - features[i].getFeatureWeight());
		}
	}
	
	/**
	 * Update personal best of a particle and the global best (if required)
	 * @param index of the particle
	 * @param heuristic at the index
	 */
	private void updateBests(int index, Heuristic heuristic) {
		if (heuristic.getFitness() > personalBests[index].getFitness()) {
			personalBests[index] = heuristic.clone();
			
			if (personalBests[index].getFitness() > globalBest.getFitness()) {
				globalBest = personalBests[index].clone();
			}
		}
	}
	
	@Override
	public void createNextGen() {
		ArrayList<Heuristic> population = cluster.getPopulation();
		
		// Move every particle
		for (int i = 0; i < popSize; i++) {
			updateVel(i, population.get(i));
			updatePos(i, population.get(i));
		}
		
		cluster.evaluateFitness();
		
		// Update personal and global bests
		for (int i = 0; i < popSize; i++) {
			updateBests(i, population.get(i));
		}
	}
}
