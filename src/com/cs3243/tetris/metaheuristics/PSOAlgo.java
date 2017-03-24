package com.cs3243.tetris.metaheuristics;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.cs3243.tetris.cluster.Cluster;
import com.cs3243.tetris.heuristics.Heuristic;
import com.cs3243.tetris.heuristics.PSOHeuristic;

public class PSOAlgo extends Metaheuristic {
	private final double OMEGA = 0.6;
	private final double RHOP = 1.4;
	private final double RHOG = 2.0;
	
	Heuristic globalBest;
	ArrayList<Heuristic> population;
	
	int popSize;
	int numFeatures;
	
	@Override
	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
		
		population = this.cluster.getPopulation();
		popSize = this.cluster.getPopSize();
		numFeatures = this.cluster.getPopulation().get(0).getNumFeatures();
		globalBest = new Heuristic();
		
		// Cannot evaluate fitness here because setCluster is called during instantiation of Island and it will delay the main thread
		// cluster.evaluateFitness(); 
		// initPositions();
	}
	
	/**
	 * Initialize personal bests with current fitness for each heuristic
	 * Update global best to be best over all the current personal bests 
	 */
	private void initPositions() {
		globalBest.setFitness(0);
		updateGlobalBest();
	}
	
	/**
	 * Update global best
	 */
	private void updateGlobalBest() {
		for (Heuristic heuristic : population) {
			if (heuristic.getFitness() > globalBest.getFitness()) {
				globalBest = heuristic.clone();
			}
		}
	}
	
	@Override
	public void createNextGen() {
		PSOHeuristic psoHeuristic;
		
		// Evaluate fitness and initialize positions if first generation
		if (globalBest == null) {
			 cluster.evaluateFitness(); 
			 initPositions();
		}
		
		// Move every particle
		for (Heuristic heuristic : population) {
			psoHeuristic = ((PSOHeuristic) heuristic);
			psoHeuristic.updateVel(OMEGA, RHOP, RHOG, globalBest);
			psoHeuristic.updatePos();
		}
		
		cluster.evaluateFitness();
		
		updateGlobalBest();
		System.out.println("Global best for " + cluster.clusterName + ": " + globalBest.getFitness());
	}
	
	@Override
	public void immmigrate(List<Heuristic> newHeuristics) {
		cluster.extraditeWorstHeuristics(newHeuristics.size());
		List<Heuristic> newPSOHeuristics = newHeuristics.stream().map(newHeuristic -> new PSOHeuristic(newHeuristic)).collect(Collectors.toList());
		cluster.immigrateHeuristics(newPSOHeuristics);
	}
}
