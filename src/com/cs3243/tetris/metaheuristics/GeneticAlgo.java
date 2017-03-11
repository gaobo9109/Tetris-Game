package com.cs3243.tetris.metaheuristics;

import java.util.ArrayList;
import java.util.Collections;

import com.cs3243.tetris.Heuristic;
import com.cs3243.tetris.features.Feature;

public class GeneticAlgo extends Metaheuristic {

	private static final double MUTATION_PROB = 0.02;
	private static final double MUTATION_MEAN = 1;
	private static final double MUTATION_STD = 15;

	@Override
	/*
	 * Keep the top few percent of the population
	 * The rest go through recombination by roulette wheel selection
	 */
	public void createNextGen() {
		if (cluster == null) return;
		
		int popSize = cluster.getPopSize();
		ArrayList<Heuristic> population = cluster.getPopulation();
		double fitnessSum = cluster.evaluateFitness();
		
		int numKept = (int)(popSize * 0.1);
		int numCrossOver = popSize - numKept;
		
		ArrayList<Heuristic> newPopulation = new ArrayList<Heuristic>();
		for(int i=0; i<numKept; i++){
			newPopulation.add(population.remove(0));
		}
		
		//finite possibility that same parent get picked twice
		Collections.shuffle(population);
		for(int i=0; i<numCrossOver; i++){
			ArrayList<Heuristic> parents = new ArrayList<Heuristic>();
			for(int j=0; j<2; j++){
				double partialSum = 0;
				double cutoff = rand.nextDouble() * fitnessSum;
				int k = 0;
				while(partialSum < cutoff){
					partialSum += population.get(k).getFitness();
					k++;
				}
				parents.add(population.get(k-1));
			}
			Heuristic child = crossover(parents.get(0), parents.get(1));
			mutate(child);
			newPopulation.add(child);
		}
		
		cluster.updatePopulation(newPopulation);
	}
	
	/**
	 * Mutate all features of heuristic
	 */
	public void mutate(Heuristic heuristic) {
		for (Feature feature : heuristic.getFeatures()) {
			feature.mutate(MUTATION_PROB, MUTATION_MEAN, MUTATION_STD);
		}
	}
	
	/**
	 * Cross-over two heuristics
	 * 
	 * @param hs1
	 * @param hs2
	 * @return new heuristic as a result of cross-over
	 */
	public static Heuristic crossover(Heuristic hs1, Heuristic hs2) {
		double ft1 = hs1.getFitness();
		double ft2 = hs2.getFitness();
		double weightage = (ft1 != 0 || ft2 != 0) ? ft1 / (ft1 + ft2) : 0.5;
		Heuristic newHeuristics = new Heuristic();

		for (int i = 0; i < hs1.getFeatures().length; i++) {
			double hs1Weight = hs1.getFeatures()[i].getFeatureWeight();
			double hs2Weight = hs2.getFeatures()[i].getFeatureWeight();
			double newWeight = weightage * hs1Weight + (1 - weightage) * hs2Weight;
			newHeuristics.getFeatures()[i].setFeatureWeight(newWeight);
		}

		return newHeuristics;

	}
}
