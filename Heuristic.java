package com.cs3243.tetris;
import java.util.Random;

import com.cs3243.tetris.features.ColWithHole;
import com.cs3243.tetris.features.DeepestWell;
import com.cs3243.tetris.features.Feature;
import com.cs3243.tetris.features.HighestCol;
import com.cs3243.tetris.features.NumHoles;
import com.cs3243.tetris.features.NumWells;
import com.cs3243.tetris.features.RowsCleared;
import com.cs3243.tetris.features.TotalColHeight;
import com.cs3243.tetris.features.TotalColHeightDiff;

/**
 * Defines features
 * Performs linear mix of feature to generate final score
 * Implements comparable
 * @author gaobo
 *
 */
public class Heuristic implements Comparable<Heuristic>{
	
	public Feature[] features = new Feature[] { // Define included features
			new RowsCleared(),
			new TotalColHeight(),
			new TotalColHeightDiff(),
			new HighestCol(),
			new NumWells(),
			new DeepestWell(),
			new NumHoles(),
			new ColWithHole()
	};
	public static final double MUTATION_PROB = 0.1;
	public static final double PERTURBATION_RANGE = 0.05;
	private double fitness;	
	
	public int calculateHeuristicScore(NextState s){
		int score = 0;
		
		for (Feature feature:features) {
			score += feature.getScore(s);
		}
		
		return score;
	}
	
	public void setFitness(double fitness){
		this.fitness = fitness;
	}
	
	public double getFitness(){
		return fitness;
	}
	
	public void mutateAll(){
		for (Feature feature:features) {
			feature.mutate(MUTATION_PROB, PERTURBATION_RANGE);
		}
	}
	
	public static Heuristic mix(Heuristic hs1, Heuristic hs2){
		double ft1 = hs1.getFitness();
		double ft2 = hs2.getFitness();
		
		double weightage = ft1 / (ft1 + ft2);
		Heuristic newHeuristics = new Heuristic();
		
		for(int i=0; i<hs1.features.length; i++){
			double hs1Weight = hs1.features[i].getFeatureWeight();
			double hs2Weight = hs2.features[i].getFeatureWeight();
			double newWeight = weightage * hs1Weight + (1-weightage) * hs2Weight;
			newHeuristics.features[i].setFeatureWeight(newWeight); 
		}
		
		return newHeuristics;
		
	}
	
	@Override
    public int compareTo(Heuristic other) {
        return (int)(this.fitness - other.getFitness());
    }
	
	
	
}
