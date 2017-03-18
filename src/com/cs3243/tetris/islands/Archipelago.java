package com.cs3243.tetris.islands;

import com.cs3243.tetris.metaheuristics.GeneticAlgo;
import com.cs3243.tetris.metaheuristics.Metaheuristic;
import com.cs3243.tetris.metaheuristics.Metaheuristic.MetaheuristicTypes;
import com.cs3243.tetris.metaheuristics.PSOAlgo;

public class Archipelago {
	private Thread c1;
	private Thread c2;
	private Thread c3;
	private Thread c4;
	
	public Archipelago(int totalPopulation) throws InstantiationException, IllegalAccessException{
		int islandPopulation = totalPopulation / 4;
		
		c1 = new Thread(new Island(new GeneticAlgo(), "c1", islandPopulation, 30, MetaheuristicTypes.GENETIC),"t1");
		c2 = new Thread(new Island(new GeneticAlgo(), "c2", islandPopulation, 30, MetaheuristicTypes.GENETIC),"t2");
		c3 = new Thread(new Island(new PSOAlgo(), "c3", islandPopulation, 30, MetaheuristicTypes.PSO),"t3");
		c4 = new Thread(new Island(new PSOAlgo(), "c4", islandPopulation, 30, MetaheuristicTypes.PSO),"t4");
	}

	public void runAlgorithm(){
		c1.start();
		c2.start();
		c3.start();
		c4.start();
	}
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException{
		Archipelago archipelago = new Archipelago(100);
		archipelago.runAlgorithm();
	}
}
