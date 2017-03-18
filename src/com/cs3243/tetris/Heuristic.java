package com.cs3243.tetris;

import com.cs3243.tetris.features.AltitudeDiff;
import com.cs3243.tetris.features.ColTransition;
import com.cs3243.tetris.features.DeepestWell;
import com.cs3243.tetris.features.Feature;
import com.cs3243.tetris.features.HighestCol;
import com.cs3243.tetris.features.NumHoles;
import com.cs3243.tetris.features.NumWells;
import com.cs3243.tetris.features.RowTransition;
import com.cs3243.tetris.features.RowsCleared;
import com.cs3243.tetris.features.WeightedBlock;
import com.cs3243.tetris.features.WellSum;

/**
 * Defines features. Performs linear mix of features to generate final score.
 * 
 * @author Advay Pal, Chang Chu-Ming, Gao Bo, Herbert Ilhan Tanujaya, Varun
 *         Gupta
 */
public class Heuristic implements Comparable<Heuristic> {

	protected Feature[] features = new Feature[] { // Define included features
			new RowsCleared(), new HighestCol(), new NumWells(),new WellSum(),
			new DeepestWell(), new NumHoles(), new WeightedBlock(), new AltitudeDiff(),
			new ColTransition(), new RowTransition()
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

		for (Feature feature : features) {
			score += feature.getScore(s);
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
		return (int) (this.fitness - other.getFitness());
	}

	public int getNumFeatures() {
		return numFeatures;
	}
}
