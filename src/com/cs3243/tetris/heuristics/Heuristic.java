package com.cs3243.tetris.heuristics;

import com.cs3243.tetris.NextState;
import com.cs3243.tetris.features.AltitudeDiff;
import com.cs3243.tetris.features.ColTransition;
import com.cs3243.tetris.features.ColWithHole;
import com.cs3243.tetris.features.DeepestWell;
import com.cs3243.tetris.features.Feature;
import com.cs3243.tetris.features.HighestCol;
import com.cs3243.tetris.features.NumHoles;
import com.cs3243.tetris.features.NumWells;
import com.cs3243.tetris.features.RowTransition;
import com.cs3243.tetris.features.RowsCleared;
import com.cs3243.tetris.features.TotalColHeight;
import com.cs3243.tetris.features.TotalColHeightDiff;
import com.cs3243.tetris.features.WeightedBlock;
import com.cs3243.tetris.features.WellSum;
import com.cs3243.tetris.metaheuristics.Metaheuristic.MetaheuristicTypes;

/**
 * Defines features. Performs linear mix of features to generate final score.
 * 
 * @author Advay Pal, Chang Chu-Ming, Gao Bo, Herbert Ilhan Tanujaya, Varun
 *         Gupta
 */
public class Heuristic implements Comparable<Heuristic> {
    
    //Very hacky
    private int[] type1Features = {1, 2, 3, 4, 5, 6, 7};
    //WHAT ABOUT 12?
    private int[] type2Features = {8, 9, 10};
    private int[] type3Features = {11, 12};

	protected Feature[] features = new Feature[] { // Define included features
	        new RowsCleared(), new AltitudeDiff(), new DeepestWell(), new HighestCol(), 
	        new NumWells(), new TotalColHeight(), new TotalColHeightDiff(), new WellSum(), 
	        new ColTransition(), new NumHoles(), new ColWithHole(), new RowTransition(), 
	        new WeightedBlock()
		};
	
	protected int numFeatures = features.length;
	
	protected double fitness;
	
	public Feature[] getFeatures() {
		return features;
	}
	
	public void setFeatures(Feature[] features) {
		this.features = features;
	}
	
	public Heuristic clone() {
		Feature[] newFeatures = new Feature[numFeatures];
		for (int i = 0; i < numFeatures; i++) {
			newFeatures[i] = this.features[i].clone();
		}
		
		Heuristic newHeuristic = new Heuristic();
		newHeuristic.features = newFeatures;
		newHeuristic.fitness = this.fitness;
		return newHeuristic;
	}

	/**
	 * Calculate score of heuristic using linear sum of features
	 * 
	 * @param s
	 * @return total score
	 */
	public double calculateHeuristicScore(NextState s) {
		int score = 0;
		int[] top = s.getTop();
		int[][] field = s.getField();
		int numRows = field.length;
		int numCols = field[0].length;
		//YAH: Yet another hack.
		features[10] = new ColWithHole(numCols);
		for (int col = 0; col < numCols; col++) {
		    //O(1) time loop
            for (int index : type1Features) {
                features[index].updateScore(s, 0, col);
            }
		    for (int row = 0; row < numRows; row ++) {
		        if (row < top[col] - 1) {
		            //O(1) time loop
		            for (int index : type2Features) {
		                features[index].updateScore(s, row, col);
		            }
		        }
		        //O(1) time loop
                for (int index : type3Features) {
                    features[index].updateScore(s, row, col);
                }
		    }
		}
		
		//1st feature needs no iterations.
		features[0].updateScore(s, 0, 0);
		score += features[0].getScore();
		
		for (Feature feature : features) {
			feature.resetScore();		
		}

		return score;
	}

	/**
	 * Set fitness score of heuristic
	 * 
	 * @param fitness
	 */
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	/**
	 * Get fitness score of heuristic
	 * 
	 * @return fitness score
	 */
	public double getFitness() {
		return fitness;
	}

	/**
	 * Compare two heuristics' fitness scores
	 */
	@Override
	public int compareTo(Heuristic other) {
		return (int) Math.signum(this.fitness - other.fitness);
	}

	public int getNumFeatures() {
		return numFeatures;
	}
	
	public static Class<?> clazzFactory(MetaheuristicTypes metaheuristicType) {
		switch (metaheuristicType) {
		case GENETIC:
			return GeneticHeuristic.class;
		case PSO:
			return PSOHeuristic.class;
		default:
			throw new IllegalArgumentException();
		}
	}
}
