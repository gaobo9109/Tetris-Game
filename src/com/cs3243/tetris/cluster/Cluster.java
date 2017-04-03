package com.cs3243.tetris.cluster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.cs3243.tetris.PlayerSkeleton;
import com.cs3243.tetris.StateStorage;
import com.cs3243.tetris.heuristics.Heuristic;
import com.cs3243.tetris.metaheuristics.Metaheuristic.MetaheuristicTypes;

public class Cluster {

	public String clusterName;
	private String fileName;
	private ArrayList<Heuristic> population;
	private int popSize;
	private PlayerSkeleton ps;
	private StateStorage storage;

	private static final int NUM_GAMES = 1;
	private static final int NUM_GAMES_TEST_BEST = 5; // Test best heuristic over say 5 games

	public Cluster(String clusterName, int popSize, MetaheuristicTypes metaheuristicType)
			throws InstantiationException, IllegalAccessException {
		this.clusterName = clusterName;
		this.fileName = clusterName + ".csv";
		this.popSize = popSize;
		storage = new StateStorage();
		population = new ArrayList<Heuristic>();
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

	private void initPopulation(MetaheuristicTypes metaheuristicType)
			throws InstantiationException, IllegalAccessException {
		Heuristic hs;
		for (int i = 0; i < popSize; i++) {
			hs = Heuristic.heuristicFactory(metaheuristicType);
			population.add(hs);
		}
	}

	public double evaluateFitness() {
		double fitnessSum = 0;
		double maxFitness = 0;
		double minFitness = Double.POSITIVE_INFINITY;
		for (int i = 0; i < popSize; i++) {
			Heuristic hs = population.get(i);
			double fitness = 0;

			for (int j = 0; j < NUM_GAMES; j++) {
				fitness += ps.playFullGame(hs, false);
			}
			fitness = fitness / NUM_GAMES;
			hs.setFitness(fitness);

			fitnessSum += fitness;
			if (fitness > maxFitness)
				maxFitness = fitness;
			else if (fitness < minFitness)
				minFitness = fitness;
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

	public void immigrateHeuristics(List<Heuristic> heuristics, MetaheuristicTypes metaheuristicType) {
		population.addAll(heuristics.stream().map(heuristic -> Heuristic.heuristicFactory(metaheuristicType, heuristic)).collect(Collectors.toList()));
	}
}
