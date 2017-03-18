package com.cs3243.tetris.heuristics;

public class GeneticHeuristic extends Heuristic {
	public GeneticHeuristic() {
		super();
	}
	
	public GeneticHeuristic(Heuristic heuristic) {
		super();
		this.features = heuristic.features;
		this.fitness = heuristic.fitness;
	}
}
