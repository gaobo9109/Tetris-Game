package com.cs3243.tetris;

import java.util.ArrayList;
import java.util.Collections;

public class Evolution {

	private ArrayList<Heuristic> population;
	private int popSize;
	private PlayerSkeleton ps;
	private int generation;

	public Evolution(int popSize, int generation) {
		this.popSize = popSize;
		this.generation = generation;
		population = new ArrayList<Heuristic>();
		ps = new PlayerSkeleton();
		initPopulation();
	}

	private void initPopulation() {
		for (int i = 0; i < popSize; i++) {
			Heuristic hs = new Heuristic();
			population.add(hs);
		}
	}

	private void createNextGen() {
		double averageFitness = 0;
		for (int i = 0; i < popSize; i++) {
			Heuristic hs = population.get(i);
			double fitness = ps.playFullGame(hs, false);
			hs.setFitness(fitness);
			averageFitness += fitness;
			System.out.println("player " + i + " has a fitness value of " + fitness);
		}

		System.out.println("average fitness of this generation is " + averageFitness / popSize);

		int cutoff = (int) (popSize * 0.3);
		Collections.sort(population);

		// remove the bottom 30%
		for (int i = 0; i < cutoff; i++) {
			population.remove(0);
		}

		cutoff = cutoff * 2;
		ArrayList<Heuristic> subset = new ArrayList<Heuristic>();

		// get the fittest 60%
		for (int i = 0; i < cutoff; i++) {
			subset.add(population.get(population.size() - i - 1));
		}

		Collections.shuffle(subset);

		for (int i = 0; i < cutoff; i += 2) {
			Heuristic hs1 = subset.get(i);
			Heuristic hs2 = subset.get(i + 1);
			Heuristic child = Heuristic.mix(hs1, hs2);
			child.mutateAll();
			population.add(child);
		}

	}

	public void runEvolution() {
		for (int i = 0; i < generation; i++) {
			System.out.println("generation " + i);
			createNextGen();
		}
	}

	public Heuristic getBestHeuristics() {
		Collections.sort(population, Collections.reverseOrder());
		return population.get(0);
	}

	public static void main(String[] args) {
		Evolution evo = new Evolution(20, 500);
		evo.runEvolution();
		Heuristic hs = evo.getBestHeuristics();
		PlayerSkeleton ps = new PlayerSkeleton();
		ps.playFullGame(hs, true);
	}
}
