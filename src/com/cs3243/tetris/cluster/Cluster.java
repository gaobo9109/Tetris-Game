package com.cs3243.tetris.cluster;

import java.util.ArrayList;
import java.util.Collections;

import com.cs3243.tetris.Heuristic;
import com.cs3243.tetris.PlayerSkeleton;
import com.cs3243.tetris.StateStorage;

public class Cluster {

	private String clusterName;
	private String fileName;
	private ArrayList<Heuristic> population;
	private int popSize;
	private PlayerSkeleton ps;
	private StateStorage storage;

	public Cluster(String clusterName, int popSize) {
		this.clusterName = clusterName;
		this.fileName = clusterName + ".csv";
		this.popSize = popSize;
		storage = new StateStorage();
		population = new ArrayList<Heuristic>();
		if(!storage.readStateFromFile(fileName,population)) initPopulation();
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

	private void initPopulation() {
		for (int i = 0; i < popSize; i++) {
			Heuristic hs = new Heuristic();
			population.add(hs);
		}
	}
	

	public double evaluateFitness() {
		
		double fitnessSum = 0;
		double maxFitness = 0;
		double minFitness = Double.POSITIVE_INFINITY;
		for (int i = 0; i < popSize; i++) {
			Heuristic hs = population.get(i);
			double fitness = ps.playFullGame(hs, false);
			hs.setFitness(fitness);
			
			System.out.println("Player fitness: " + fitness);
			
			fitnessSum += fitness;
			if(fitness > maxFitness) maxFitness = fitness;
			else if(fitness < minFitness) minFitness = fitness;
		}
		
		System.out.println("========================");
		System.out.println("Population Statistics");
		System.out.println("Max fitness: " + maxFitness);
		System.out.println("Min fitness: " + minFitness);
		System.out.println("Average fitness: " + fitnessSum / popSize);
		System.out.println("========================");
		
		Collections.sort(population);
		return fitnessSum;
	}
	
	public void writeStateToFile() {
		storage.writeStateToFile(population, fileName);
	}

	public Heuristic getBestHeuristics() {
		Collections.sort(population, Collections.reverseOrder());
		return population.get(0);
	}
}