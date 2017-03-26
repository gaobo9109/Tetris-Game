package com.cs3243.tetris.heuristics;

import com.cs3243.tetris.features.Feature;

public class GeneticHeuristic extends Heuristic {
	private static final double MUTATION_PROB = 0.02;
	private static final double MUTATION_MEAN = 1;
	private static final double MUTATION_STD = 1;
	
	public GeneticHeuristic() {
		super();
	}
	
	public GeneticHeuristic(Heuristic heuristic) {
		super();
		this.features = heuristic.features;
		this.fitness = heuristic.fitness;
	}
	
	/**
	 * Mutate all features of heuristic
	 */
	public void mutate() {
		for (Feature feature : features) {
			mutateFeature(feature);
		}
	}
	
	private void mutateFeature(Feature feature) {
		boolean mutate = random.nextDouble() < MUTATION_PROB;
		if (mutate) {
			feature.setFeatureWeight(
					feature.getFeatureWeight() * (random.nextGaussian() * MUTATION_STD + MUTATION_MEAN)
				);
		}
	}
	
	/**
	 * Cross-over two heuristics
	 * 
	 * @param hs1
	 * @param hs2
	 * @return new heuristic as a result of cross-over
	 */
	public static GeneticHeuristic crossover(GeneticHeuristic hs1, GeneticHeuristic hs2) {
		GeneticHeuristic newHeuristics = new GeneticHeuristic();
		
//		double ft1 = hs1.getFitness();
//		double ft2 = hs2.getFitness();
//		double weightage = (ft1 != 0 || ft2 != 0) ? ft1 / (ft1 + ft2) : 0.5;

		for (int i = 0; i < hs1.getFeatures().length; i++) {
			double hs1Weight = hs1.getFeatures()[i].getFeatureWeight();
  			double hs2Weight = hs2.getFeatures()[i].getFeatureWeight();
//			double newWeight = weightage * hs1Weight + (1 - weightage) * hs2Weight;
//			newHeuristics.getFeatures()[i].setFeatureWeight(newWeight);
			newHeuristics.getFeatures()[i].setFeatureWeight(random.nextDouble() < 0.5 ? hs1Weight : hs2Weight);
		}

		return newHeuristics;

	@Override
	public Heuristic convertHeuristic() {
		return this;
	}
}
