package com.cs3243.tetris.metaheuristics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.cs3243.tetris.heuristics.GeneticHeuristic;
import com.cs3243.tetris.heuristics.Heuristic;

public class GeneticAlgo extends Metaheuristic {

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
			GeneticHeuristic child = GeneticHeuristic.crossover((GeneticHeuristic) parents.get(0), (GeneticHeuristic) parents.get(1));
			child.mutate();
			newPopulation.add(child);
		}
		
		cluster.updatePopulation(newPopulation);
	}

	@Override
	public void immmigrate(List<Heuristic> newHeuristics) {
		cluster.extraditeWorstHeuristics(newHeuristics.size());
		List<Heuristic> newGeneticHeuristics = newHeuristics.stream().map(newHeuristic -> new GeneticHeuristic(newHeuristic)).collect(Collectors.toList());
		cluster.immigrateHeuristics(newGeneticHeuristics);
	}
}
