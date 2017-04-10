


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import mpi.MPI;
import mpi.MPIException;

public class Cluster {

	public String clusterName;
	private String fileName;
	private ArrayList<Heuristic> population;
	private Metaheuristic.MetaheuristicTypes metaheuristicType;
	private int popSize;
	private PlayerSkeleton ps;
	private StateStorage storage;

	private static final int NUM_GAMES = 1;
	private static final int NUM_GAMES_TEST_BEST = 5; // Test best heuristic over say 5 games

	public Cluster(String clusterName, int popSize, Metaheuristic.MetaheuristicTypes metaheuristicType)
			throws InstantiationException, IllegalAccessException {
		this.clusterName = clusterName;
		this.fileName = clusterName + ".csv";
		this.popSize = popSize;
		storage = new StateStorage();
		population = new ArrayList<Heuristic>();
		this.metaheuristicType = metaheuristicType;
		if (!storage.readStateFromFile(fileName, population, metaheuristicType))
			initPopulation(metaheuristicType);
		ps = new PlayerSkeleton();
	}

	public int getPopSize() {
		return popSize;
	}

	public ArrayList<Heuristic> getPopulation() {
		return population;
	}

	public void updatePopulation(ArrayList<Heuristic> population) {
		this.population = population;
	}

	private void initPopulation(Metaheuristic.MetaheuristicTypes metaheuristicType)
			throws InstantiationException, IllegalAccessException {
		Heuristic hs;
		for (int i = 0; i < popSize; i++) {
			hs = Heuristic.heuristicFactory(metaheuristicType);
			population.add(hs);
		}
	}

	public double evaluateFitness() throws MPIException {
		double fitnessSum = 0;
		double maxFitness = 0;
		double minFitness = Double.POSITIVE_INFINITY;
//		for (int i = 0; i < popSize; i++) {
//			Heuristic hs = population.get(i);
//			double fitness = 0;
//
////			for (int j = 0; j < NUM_GAMES; j++) {
////				fitness += ps.playFullGame(hs, false);
////			}
//			
//			fitness = fitness / NUM_GAMES;
//			hs.setFitness(fitness);
//
//			fitnessSum += fitness;
//			if (fitness > maxFitness)
//				maxFitness = fitness;
//			else if (fitness < minFitness)
//				minFitness = fitness;
//		}
		
//		int num_children = MPI.COMM_WORLD.getSize() - 1;
//		double[] heuristicWeights = new double[13 * num_children];
//		for (int i = 0; i < popSize; i++) {
//			Feature[] features = population.get(i).getFeatures();
//			for (int j = 0; j < 13; j++) {
//				heuristicWeights[i * 13 + j] = features[j].getFeatureWeight();
//			}
//		}
//		
//		double[] receiveWeights = new double[13];
//		
//		MPI.COMM_WORLD.scatter(heuristicWeights, 13, MPI.DOUBLE, receiveWeights, 13, MPI.DOUBLE, 0);
//		Heuristic newHeuristic = new Heuristic(receiveWeights);
//		
//		double[] thisfitness = new double[1];
//		thisfitness[0] = ps.playFullGame(newHeuristic, false);
//		double fitnesses[] = new double[popSize];
//		MPI.COMM_WORLD.gather(thisfitness, 1, MPI.DOUBLE, fitnesses, 1, MPI.DOUBLE, 0);
//		
//		for (int i = 0; i < fitnesses.length; i++) {
//			System.out.println(fitnesses[i]);
//		}
		
		int numChildren = MPI.COMM_WORLD.getSize() - 1;
		double[] heuristicWeights = new double[13];
		int startHeuristic = 0;
		
		while (startHeuristic < popSize) {
			for (int childRank = 1; childRank < numChildren + 1; childRank++) {
				int currentHeuristic = startHeuristic + childRank - 1;
				if (currentHeuristic == popSize) break;
				
				Feature[] features = population.get(currentHeuristic).getFeatures();
				for (int j = 0; j < 13; j++) {
					heuristicWeights[j] = features[j].getFeatureWeight();
				}
				
				MPI.COMM_WORLD.send(heuristicWeights, 13, MPI.DOUBLE, childRank, 1);
			}
			
			double[] fitnessArr = new double[1];
			double fitness;
			
			for (int childRank = 1; childRank < numChildren + 1; childRank++) {
				int currentHeuristic = startHeuristic + childRank - 1;
				if (currentHeuristic == popSize) break;
				
				MPI.COMM_WORLD.recv(fitnessArr, 1, MPI.DOUBLE, childRank, 1);
				fitness = fitnessArr[0];
				
//				System.out.println(childRank + ": " + fitness);
				
				population.get(currentHeuristic).setFitness(fitness);
				fitnessSum += fitness;
				if (fitness > maxFitness) {
					maxFitness = fitness;
				}
				if (fitness < minFitness) {
					minFitness = fitness;
				}
			}
			
			startHeuristic += numChildren;
		}
		
		Collections.sort(population, Collections.reverseOrder());
		Heuristic bestHeuristic = population.get(0);
//		double bestFitness = 0;
//		for (int j = 0; j < NUM_GAMES_TEST_BEST; j++) {
//			bestFitness += ps.playFullGame(bestHeuristic, false);
//		}

		System.out.println("Cluster:" + clusterName);
		System.out.println("Population Statistics");
		System.out.println("Max fitness: " + maxFitness);
		System.out.println("Min fitness: " + minFitness);
		System.out.println("Average fitness: " + fitnessSum / popSize);
//		System.out.println("Best Heuristic, over " + NUM_GAMES_TEST_BEST + " games: " + bestFitness / NUM_GAMES_TEST_BEST);
		System.out.println("Best Heuristic: " + bestHeuristic.toString());
		System.out.println("========================");

		return fitnessSum;
	}

	public void writeStateToFile() {
		storage.writeStateToFile(population, fileName);
	}

	public List<Heuristic> emigrateHeuristics(int numToGet) {
		Collections.sort(population, Collections.reverseOrder());
		return population.subList(0, numToGet).stream().map(heuristic -> heuristic.clone())
				.collect(Collectors.toList());
	}

	public void extraditeWorstHeuristics(int numToRemove) {
		Collections.sort(population);
		population.removeAll(population.subList(0, numToRemove));
	}

	public void immigrateHeuristics(List<Heuristic> heuristics) {
		population.addAll(heuristics.stream().map(heuristic -> Heuristic.heuristicFactory(metaheuristicType, heuristic)).collect(Collectors.toList()));
	}
}
