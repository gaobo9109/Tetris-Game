



import java.util.Random;

import mpi.MPIException;

public abstract class Metaheuristic {
	protected Random rand = new Random();
	protected Cluster cluster = null;

	public enum MetaheuristicTypes {
		GENETIC, PSO
	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	public Cluster getCluster() {
		return cluster;
	}

	abstract public void createNextGen() throws MPIException;
//	abstract public List<Heuristic> emigrateHeuristics(int numToGet);
//	abstract public void extraditeWorstHeuristics(int numToRemove);
//	abstract public void immigrateHeuristics(List<Heuristic> heuristics);
}
