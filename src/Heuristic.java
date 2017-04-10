


import java.util.Random;


/**
 * Defines features. Performs linear mix of features to generate final score.
 * 
 * @author Advay Pal, Chang Chu-Ming, Gao Bo, Herbert Ilhan Tanujaya, Varun
 *         Gupta
 */
public class Heuristic implements Comparable<Heuristic> {

	// Very hacky
	private int[] type1Features = { 1, 2, 3, 4, 5, 6, 7 };
	// WHAT ABOUT 12?
	private int[] type2Features = { 8, 9, 10 };
	private int[] type3Features = { 11, 12 };

	protected Feature[] features = new Feature[] { // Define included features
			new RowsCleared(), new AltitudeDiff(), new DeepestWell(), new HighestCol(), new NumWells(),
			new TotalColHeight(), new TotalColHeightDiff(), new WellSum(), new ColTransition(), new NumHoles(),
			new ColWithHole(), new RowTransition(), new WeightedBlock() };

	protected int numFeatures = features.length;

	protected double fitness;

	protected static Random random = new Random();
	
	public Heuristic() {}
	
	public Heuristic(double[] featureWeights) {
		for (int i = 0; i < featureWeights.length; i++) {
			features[i].setFeatureWeight(featureWeights[i]);
		}
	}

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
	 * @param ns
	 * @return total score
	 */
	public double calculateHeuristicScore(State s, NextState ns) {
		double score = 0;
		int[] top = ns.getTop();
		int[][] field = ns.getField();
		int numRows = field.length;
		int numCols = field[0].length;
		// YAH: Yet another hack.
		features[10] = new ColWithHole(numCols);
		for (int col = 0; col < numCols; col++) {
			// O(1) time loop
			for (int index : type1Features) {
				features[index].updateScore(ns, 0, col);
			}
			for (int row = 0; row < numRows; row++) {
				if (row < top[col] - 1) {
					// O(1) time loop
					for (int index : type2Features) {
						features[index].updateScore(ns, row, col);
					}
				}
				// O(1) time loop
				for (int index : type3Features) {
					features[index].updateScore(ns, row, col);
				}
			}
		}

		// 1st feature needs no iterations.
		features[0].updateScore(s, ns, 0, 0);

		for (Feature feature : features) {
			score += feature.getScore();
		}

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

	public static Heuristic heuristicFactory(Metaheuristic.MetaheuristicTypes metaheuristicType) {
		switch (metaheuristicType) {
		case GENETIC:
			return new GeneticHeuristic();
		case PSO:
			return new PSOHeuristic();
		default:
			throw new IllegalArgumentException();
		}
	}
	
	public static Heuristic heuristicFactory(Metaheuristic.MetaheuristicTypes metaheuristicType, Heuristic heuristic) {
		switch (metaheuristicType) {
		case GENETIC:
			return new GeneticHeuristic(heuristic);
		case PSO:
			return new PSOHeuristic(heuristic);
		default:
			throw new IllegalArgumentException();
		}
	}

	@Override
	public String toString() {
		StringBuilder line = new StringBuilder();
		double weight;

		for (int i = 0; i < features.length; i++) {
			weight = features[i].getFeatureWeight();
			line.append(weight);

			if (i == features.length - 1) {
				line.append("\n");
			} else {
				line.append(",");
			}
		}

		return line.toString();
	}
}
