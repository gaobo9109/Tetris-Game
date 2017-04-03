package com.cs3243.tetris.islands;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.cs3243.tetris.metaheuristics.GeneticAlgo;
import com.cs3243.tetris.metaheuristics.Metaheuristic.MetaheuristicTypes;
import com.cs3243.tetris.metaheuristics.PSOAlgo;

public class Archipelago {
	private Island c1;
	private Island c2;
	private Island c3;
	private Island c4;
	
	int islandPopulation;
	
	public Archipelago(int totalPopulation) throws InstantiationException, IllegalAccessException{
		islandPopulation = totalPopulation / 4;
		
		c1 = new Island(new GeneticAlgo(), "c1", islandPopulation, MetaheuristicTypes.GENETIC);
		c2 = new Island(new GeneticAlgo(), "c2", islandPopulation, MetaheuristicTypes.GENETIC);
		c3 = new Island(new PSOAlgo(),     "c3", islandPopulation, MetaheuristicTypes.PSO);
		c4 = new Island(new PSOAlgo(),     "c4", islandPopulation, MetaheuristicTypes.PSO);
	}

	public void runAlgorithm() throws InterruptedException{
		int numGens = 1000;
		
		for (int i = 0; i < numGens; i++) {
			System.out.println("Generation " + i);
			
			ExecutorService executor = Executors.newFixedThreadPool(4);
			
			executor.execute(c1);
//			executor.execute(c2);
			executor.execute(c3);
//			executor.execute(c4);
			
			executor.shutdown();
			executor.awaitTermination(1000, TimeUnit.MINUTES);
			
			c1.exchangeHeuristics(c3, islandPopulation / 10);
		}
	}
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, InterruptedException{
		Archipelago archipelago = new Archipelago(400);
		archipelago.runAlgorithm();
	}
}
