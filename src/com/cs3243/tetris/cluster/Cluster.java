package com.cs3243.tetris.cluster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

	public Cluster(String clusterName, int popSize, MetaheuristicTypes metaheuristicType) throws InstantiationException, IllegalAccessException {
		this.clusterName = clusterName;
		this.fileName = clusterName + ".csv";
		this.popSize = popSize;
		storage = new StateStorage();
		population = new ArrayList<Heuristic>();
		if(!storage.readStateFromFile(fileName,population)) initPopulation(metaheuristicType);
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

	private void initPopulation(MetaheuristicTypes metaheuristicType) throws InstantiationException, IllegalAccessException {
		for (int i = 0; i < popSize; i++) {
			Heuristic hs = (Heuristic) Heuristic.clazzFactory(metaheuristicType).newInstance();
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
//			System.out.println("Player heuristics: " + hs.toString());
			
			fitnessSum += fitness;
			if(fitness > maxFitness) maxFitness = fitness;
			else if(fitness < minFitness) minFitness = fitness;
		}
		
		System.out.println("========================");
		System.out.println("Cluster:" + clusterName);
		System.out.println("Population Statistics");
		System.out.println("Max fitness: " + maxFitness);
		System.out.println("Min fitness: " + minFitness);
		System.out.println("Average fitness: " + fitnessSum / popSize);
		System.out.println("========================");
		
		return fitnessSum;
	}
	
	public void writeStateToFile() {
		storage.writeStateToFile(population, fileName);
	}

	public List<Heuristic> getBestHeuristics(int numToGet) {
		Collections.sort(population, Collections.reverseOrder());
		return population.subList(0, numToGet);
	}
	
	public void extraditeWorstHeuristics(int numToRemove) {
		Collections.sort(population);
		population.removeAll(population.subList(0, numToRemove));
	}
	
	public void immigrateHeuristics(List<Heuristic> heuristics) {
		population.addAll(heuristics);
	}
}
