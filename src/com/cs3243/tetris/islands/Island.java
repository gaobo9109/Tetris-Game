package com.cs3243.tetris.islands;

import java.util.List;

import com.cs3243.tetris.cluster.Cluster;
import com.cs3243.tetris.heuristics.Heuristic;
import com.cs3243.tetris.metaheuristics.Metaheuristic;
import com.cs3243.tetris.metaheuristics.Metaheuristic.MetaheuristicTypes;

public class Island implements Runnable {
	private Metaheuristic metaheuristic;

	public Island(Metaheuristic metaheuristic, String clusterName, int populationSize,
			      MetaheuristicTypes metaheuristicType) throws InstantiationException, IllegalAccessException {
		this.metaheuristic = metaheuristic;
		this.metaheuristic.setCluster(new Cluster(clusterName, populationSize, metaheuristicType));
	}

	public void runOneGen() {
		metaheuristic.createNextGen();
		metaheuristic.getCluster().writeStateToFile();
	}
	
	public void exchangeHeuristics(Island island, int numToExchange) {
		List<Heuristic> thisBestHeuristics  = this.metaheuristic.emigrateHeuristics(numToExchange);
		List<Heuristic> otherBestHeuristics = island.metaheuristic.emigrateHeuristics(numToExchange);
		
		this.metaheuristic.extraditeWorstHeuristics(numToExchange);
		island.metaheuristic.extraditeWorstHeuristics(numToExchange);
		
		this.metaheuristic.immigrateHeuristics(otherBestHeuristics);
		island.metaheuristic.immigrateHeuristics(thisBestHeuristics);
	}

	public void run() {
		runOneGen();
	}
}
