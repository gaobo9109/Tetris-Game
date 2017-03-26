package com.cs3243.tetris.islands;

import com.cs3243.tetris.cluster.Cluster;
import com.cs3243.tetris.metaheuristics.Metaheuristic;
import com.cs3243.tetris.metaheuristics.Metaheuristic.MetaheuristicTypes;

public class Island implements Runnable {
	private Metaheuristic metaheuristic;
	private int numGens;

	public Island(Metaheuristic metaheuristic, String clusterName, int populationSize, int numGens,
			MetaheuristicTypes metaheuristicType) throws InstantiationException, IllegalAccessException {
		Cluster cluster = new Cluster(clusterName, populationSize, metaheuristicType);
		this.metaheuristic = metaheuristic;
		this.metaheuristic.setCluster(cluster);
		this.numGens = numGens;
	}

	public void runOneGen() {
		metaheuristic.createNextGen();
		metaheuristic.getCluster().writeStateToFile();
	}

	public void run() {
		for (int i = 0; i < numGens; i++) {
			System.out.println("========================");
			System.out.println("Generation " + i);
			runOneGen();
		}
	}
}
