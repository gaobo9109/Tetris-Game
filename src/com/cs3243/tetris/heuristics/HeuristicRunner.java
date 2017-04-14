package com.cs3243.tetris.heuristics;

import com.cs3243.tetris.PlayerSkeleton;

public class HeuristicRunner implements Runnable {
	private PlayerSkeleton playerSkeleton = new PlayerSkeleton();
	private Heuristic heuristic;
	private int numGames;
	private double fitness;
	
	public HeuristicRunner(Heuristic heuristic, int numGames) {
		this.heuristic = heuristic;
		this.numGames = numGames;
	}

	public void run() {
		fitness = 0;

		for (int j = 0; j < numGames; j++) {
			fitness += playerSkeleton.playFullGame(heuristic, false);
		}
		fitness = fitness / numGames;
		heuristic.setFitness(fitness);
	}
	
	public double getFitness() {
		return fitness;
	}
}
