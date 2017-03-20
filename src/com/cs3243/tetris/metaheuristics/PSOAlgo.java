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
	ArrayList<Heuristic> population = this.cluster.getPopulation();
	
	int popSize;
	int numFeatures;
	
	@Override
	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
		
		popSize = this.cluster.getPopSize();
		numFeatures = this.cluster.getPopulation().get(0).getNumFeatures();
		
		cluster.evaluateFitness();
		
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
		
		// Move every particle
		for (Heuristic heuristic : population) {
			psoHeuristic = ((PSOHeuristic) heuristic);
			psoHeuristic.updateVel(OMEGA, RHOP, RHOG, globalBest);
			psoHeuristic.updatePos();
		}
		
		cluster.evaluateFitness();
		
		updateGlobalBest();
	}
	
	@Override
	public void immmigrate(List<Heuristic> newHeuristics) {
		cluster.extraditeWorstHeuristics(newHeuristics.size());
		List<Heuristic> psoHeuristics = newHeuristics.stream().map(newHeuristic -> new PSOHeuristic(newHeuristic)).collect(Collectors.toList());
		cluster.immigrateHeuristics(psoHeuristics);
	}
}
