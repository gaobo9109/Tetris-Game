


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import mpi.Comm;
import mpi.MPI;
import mpi.MPIException;
import mpi.Status;

//import com.Metaheuristic;

public class Archipelago {
	private Island c1;
	private Island c2;
	private Island c3;
	private Island c4;
	
	int islandPopulation;
	
	public Archipelago(int totalPopulation) throws InstantiationException, IllegalAccessException{
		islandPopulation = totalPopulation / 4;
		
		c1 = new Island(new GeneticAlgo(), "c1", islandPopulation, Metaheuristic.MetaheuristicTypes.GENETIC);
		c2 = new Island(new GeneticAlgo(), "c2", islandPopulation, Metaheuristic.MetaheuristicTypes.GENETIC);
		c3 = new Island(new PSOAlgo(),     "c3", islandPopulation, Metaheuristic.MetaheuristicTypes.PSO);
		c4 = new Island(new PSOAlgo(),     "c4", islandPopulation, Metaheuristic.MetaheuristicTypes.PSO);
	}

	public void runAlgorithm() throws InterruptedException, MPIException{
		int numGens = 100;
		
		for (int i = 0; i < numGens; i++) {
			System.out.println("Generation " + i);
			
//			ExecutorService executor = Executors.newFixedThreadPool(4);
			
//			executor.execute(c1);
//			executor.execute(c2);
//			executor.execute(c3);
//			executor.execute(c4);
			
//			executor.shutdown();
//			executor.awaitTermination(1000, TimeUnit.MINUTES);
			c1.runOneGen();
			c3.runOneGen();
			
			c1.exchangeHeuristics(c3, islandPopulation / 10);
		}
	}
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, InterruptedException, MPIException{
		MPI.Init(args);
		int rank = MPI.COMM_WORLD.getRank();
		
		if (rank == 0) {
			Archipelago archipelago = new Archipelago(400);
			archipelago.runAlgorithm();
		} else {
			PlayerSkeleton ps = new PlayerSkeleton();
			double[] featureWeights = new double[13];
			
			while (true) {
				MPI.COMM_WORLD.recv(featureWeights, 13, MPI.DOUBLE, 0, 1);
				Heuristic heuristic = new Heuristic(featureWeights);
				
				double[] fitness = new double[1];
				fitness[0] = ps.playFullGame(heuristic, false);
				MPI.COMM_WORLD.send(fitness, 1, MPI.DOUBLE, 0, 1);
			}
			
		}
		
		MPI.Finalize();
	}
}
