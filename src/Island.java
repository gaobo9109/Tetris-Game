


import java.util.List;

import mpi.MPIException;

//import com.Metaheuristic.MetaheuristicTypes;

public class Island implements Runnable {
	private Metaheuristic metaheuristic;
	private Cluster cluster;

	public Island(Metaheuristic metaheuristic, String clusterName, int populationSize,
			Metaheuristic.MetaheuristicTypes metaheuristicType) throws InstantiationException, IllegalAccessException {
		this.cluster = new Cluster(clusterName, populationSize, metaheuristicType);
		this.metaheuristic = metaheuristic;
		this.metaheuristic.setCluster(cluster);
	}

	public void runOneGen() throws MPIException {
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
		} catch (MPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
