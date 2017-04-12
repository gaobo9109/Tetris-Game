package com.cs3243.tetris.islands;

import java.util.List;

import com.cs3243.tetris.cluster.Cluster;
import com.cs3243.tetris.heuristics.Heuristic;
import com.cs3243.tetris.metaheuristics.Metaheuristic;
import com.cs3243.tetris.metaheuristics.Metaheuristic.MetaheuristicTypes;

public class Island implements Runnable {
	private Metaheuristic metaheuristic;
	private Cluster cluster;

	public Island(Metaheuristic metaheuristic, String clusterName, int populationSize,
			      MetaheuristicTypes metaheuristicType) throws InstantiationException, IllegalAccessException {
		this.cluster = new Cluster(clusterName, populationSize, metaheuristicType);
		this.metaheuristic = metaheuristic;
		this.metaheuristic.setCluster(cluster);
	}

	public void runOneGen() throws InterruptedException {
		metaheuristic.createNextGen();
		metaheuristic.getCluster().writeStateToFile();
	}
	
	public void exchangeHeuristics(Island island, int numToExchange) {
		List<Heuristic> thisBestHeuristics  = this.cluster.emigrateHeuristics(numToExchange);
		List<Heuristic> otherBestHeuristics = island.cluster.emigrateHeuristics(numToExchange);
		
		this.cluster.extraditeWorstHeuristics(numToExchange);
		island.cluster.extraditeWorstHeuristics(numToExchange);
		
		this.cluster.immigrateHeuristics(otherBestHeuristics);
		island.cluster.immigrateHeuristics(thisBestHeuristics);
	}

	public void run() {
		try {
			runOneGen();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
