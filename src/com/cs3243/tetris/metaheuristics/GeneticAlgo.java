package com.cs3243.tetris.metaheuristics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.cs3243.tetris.heuristics.GeneticHeuristic;
import com.cs3243.tetris.heuristics.Heuristic;

public class GeneticAlgo extends Metaheuristic {
	double FRAC_PARENTS_KEPT = 0.5;

	@Override
	/*
	 * Keep the top few percent of the population The rest go through
	 * recombination by roulette wheel selection
	 */
	public void createNextGen() {
		if (cluster == null)
			return;

		int popSize = cluster.getPopSize();
		ArrayList<Heuristic> population = cluster.getPopulation();
		double fitnessSum = cluster.evaluateFitness();

		int numKept = (int) (popSize * FRAC_PARENTS_KEPT);
		int numCrossOver = popSize - numKept;

		ArrayList<Heuristic> newPopulation = new ArrayList<Heuristic>();
		Collections.sort(population, Collections.reverseOrder());
		newPopulation.addAll(population.subList(0, numKept));

		Heuristic parent1 = null, parent2 = null;

		for (int i = 0; i < numCrossOver; i++) {
			for (int j = 0; j < 2; j++) {
				double partialSum = 0;
				double cutoff = rand.nextDouble() * fitnessSum;
				int k = 0;

				// If rand.nextDouble is so close to 1 that cutoff rounds
				// off to fitnessSum, k will become popSize, hence the k <
				// popSize check
				while (partialSum <= cutoff & k < popSize) {
					partialSum += population.get(k).getFitness();
					k++;
				}

				if (j == 0) {
					parent1 = population.get(k - 1);
				} else {
					parent2 = population.get(k - 1);
				}
			}

			GeneticHeuristic child = GeneticHeuristic.crossover((GeneticHeuristic) parent1, (GeneticHeuristic) parent2);
			child.mutate();
			newPopulation.add(child);
		}

		cluster.updatePopulation(newPopulation);
	}

//	@Override
//	public List<Heuristic> emigrateHeuristics(int numToGet) {
//		return cluster.emigrateHeuristics(numToGet);
//	}
//
//	@Override
//	public void extraditeWorstHeuristics(int numToRemove) {
//		cluster.extraditeWorstHeuristics(numToRemove);
//	}
//
//	@Override
//	public void immigrateHeuristics(List<Heuristic> heuristics) {
//		cluster.immigrateHeuristics(heuristics, MetaheuristicTypes.GENETIC);
//	}
}
