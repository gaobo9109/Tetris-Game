package com.cs3243.tetris;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Cluster implements Runnable {

	private String clusterName;
	private String fileName;
	private ArrayList<Heuristic> population;
	private int popSize;
	private PlayerSkeleton ps;
	private int generation = 30;
	private StateStorage storage;
	private Random rand = new Random();

	public Cluster(String clusterName, int popSize) {
		this.clusterName = clusterName;
		this.fileName = clusterName + ".csv";
		this.popSize = popSize;
		storage = new StateStorage();
		population = new ArrayList<Heuristic>();
		if(!storage.readStateFromFile(fileName,population)) initPopulation();
		ps = new PlayerSkeleton();
	}

	private void initPopulation() {
		for (int i = 0; i < popSize; i++) {
			Heuristic hs = new Heuristic();
			population.add(hs);
		}
	}
	

	private double evaluateFitness() {
		
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
	
	/*
	 * Keep the top few percent of the population
	 * The rest go through recombination by roulette wheel selection
	 */
	
	private void createNextGen(double fitnessSum){
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
			Heuristic child = Heuristic.mix(parents.get(0), parents.get(1));
			newPopulation.add(child);
		}
		population = newPopulation;
	}

	private void runEvolution() {
		for (int i = 0; i < generation; i++) {
//			System.out.println("generation " + i);
			storage.writeStateToFile(population,fileName);
			double fitnessSum = evaluateFitness();
			createNextGen(fitnessSum);
		}
	}

	public Heuristic getBestHeuristics() {
		Collections.sort(population, Collections.reverseOrder());
		return population.get(0);
	}

	@Override
	public void run() {
//		createNextGen();
		runEvolution();
	}

	public static void main(String[] args) {
		Cluster cluster = new Cluster("cluster",25);
		cluster.runEvolution();
		Heuristic hs = cluster.getBestHeuristics();
		PlayerSkeleton ps = new PlayerSkeleton();
		ps.playFullGame(hs, true);
	}
}
