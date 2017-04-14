package com.cs3243.tetris;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.cs3243.tetris.features.Feature;
import com.cs3243.tetris.heuristics.Heuristic;

/**
 * Player Skeleton Methods for playing game
 *
 * @author Advay Pal, Chang Chu-Ming, Gao Bo, Herbert Ilhan Tanujaya, Varun
 *         Gupta
 */
public class PlayerSkeleton {

	// implement this function to have a working system
	public int pickMove(State s, int[][] legalMoves, NextState ns, Heuristic hs) {
		double maxScore = Double.NEGATIVE_INFINITY;
		int maxIndex = 0;

		for (int i = 0; i < legalMoves.length; i++) {
			boolean notLost = ns.generateNextState(s, legalMoves[i]);
			if (!notLost) {
				continue;
			}

			double score = hs.calculateHeuristicScore(s, ns);

			if (score > maxScore) {
				maxScore = score;
				maxIndex = i;
			}
		}

		return maxIndex;
	}
	
	/**
	 * Play the game with or without displaying GUI
	 * 
	 * @param Heuristic hs
	 * @param whether to display GUI
	 * @return fitness value of this heuristic 
	 */

	public double playFullGame(Heuristic hs, boolean graphic) {
		State s = new State();
		NextState ns = new NextState();
		// Heuristics hs = new Heuristics();
		if (graphic) {
			new TFrame(s);
		}

		PlayerSkeleton p = new PlayerSkeleton();
		while (!s.hasLost()) {
			s.makeMove(p.pickMove(s, s.legalMoves(), ns, hs));
		}
		return s.getRowsCleared();
	}

	public static void main(String[] args) throws IOException {
//		State s = new State();
//		NextState ns = new NextState();
		Heuristic hs = new Heuristic();
		BufferedReader fr = new BufferedReader(new FileReader("heuristic.txt"));
		
		String line = fr.readLine();
		String[] tokens = line.split(",");
		fr.close();
		
		Feature[] features = hs.getFeatures();
		
		for (int i = 0; i < features.length; i++){
			double weight = Double.parseDouble(tokens[i]);
			features[i].setFeatureWeight(weight);
		}
		
//		new TFrame(s);
		PlayerSkeleton p = new PlayerSkeleton();
		
		double totalScore = 0;
		double numGames = 200;
		double bestScore = 0;
		double worstScore = Double.MAX_VALUE;
		double currentScore;
		
		for (int i = 0; i < numGames; i++) {
			currentScore = p.playFullGame(hs, false);
			System.out.println(currentScore);
			totalScore += currentScore;
			bestScore = bestScore > currentScore ? bestScore : currentScore;
			worstScore = worstScore < currentScore ? worstScore : currentScore;
		}
		
		System.out.println();
		System.out.println("Heuristic: " + hs);
		System.out.println("Best score: " + bestScore);
		System.out.println("Worst score: " + worstScore);
		System.out.println("Average score: " + totalScore / numGames);
		
//		while (!s.hasLost()) {
//			s.makeMove(p.pickMove(s, s.legalMoves(), ns, hs));
//			if (s.getRowsCleared() % 10000 == 0) {
//				System.out.println(s.getRowsCleared());
//			}
//			s.draw();
//			s.drawNext(0, 0);
//			try {
//				Thread.sleep(300);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//		System.out.println("You have completed " + s.getRowsCleared() + " rows.");
	}
}

package com.cs3243.tetris;

/**
 * Generates next state of the game. Useful for calculating score of next state
 * 
 * @author Advay Pal, Chang Chu-Ming, Gao Bo, Herbert Ilhan Tanujaya, Varun
 *         Gupta
 */

public class NextState {
	private int[][] field = new int[State.ROWS][State.COLS];
	private int[] top = new int[State.COLS];
	private static int rowsCleared = 0;

	private int[][][] pBottom = State.getpBottom();
	private int[][] pHeight = State.getpHeight();
	private int[][] pWidth = State.getpWidth();
	private int[][][] pTop = State.getpTop();

	public boolean generateNextState(State s, int[] legalMoves) {
		int orient = legalMoves[0];
		int slot = legalMoves[1];
		int nextPiece = s.getNextPiece();

		copyState(s);

		// height if the first column makes contact
		int height = top[slot] - pBottom[nextPiece][orient][0];
		// for each column beyond the first in the piece
		for (int c = 1; c < pWidth[nextPiece][orient]; c++) {
			height = Math.max(height, top[slot + c] - pBottom[nextPiece][orient][c]);
		}

		// check if game ended
		if (height + pHeight[nextPiece][orient] >= State.ROWS) {
			return false;
		}

		// for each column in the piece - fill in the appropriate blocks
		for (int i = 0; i < pWidth[nextPiece][orient]; i++) {

			// from bottom to top of brick
			for (int h = height + pBottom[nextPiece][orient][i]; h < height + pTop[nextPiece][orient][i]; h++) {
				field[h][i + slot] = 1;
			}
		}

		// adjust top
		for (int c = 0; c < pWidth[nextPiece][orient]; c++) {
			top[slot + c] = height + pTop[nextPiece][orient][c];
		}

		rowsCleared = 0;

		// check for full rows - starting at the top
		for (int r = height + pHeight[nextPiece][orient] - 1; r >= height; r--) {
			// check all columns in the row
			boolean full = true;
			for (int c = 0; c < State.COLS; c++) {
				if (field[r][c] == 0) {
					full = false;
					break;
				}
			}
			// if the row was full - remove it and slide above stuff down
			if (full) {
				rowsCleared++;

				// for each column
				for (int c = 0; c < State.COLS; c++) {

					// slide down all bricks
					for (int i = r; i < top[c]; i++) {
						field[i][c] = field[i + 1][c];
					}
					// lower the top
					top[c]--;
					while (top[c] >= 1 && field[top[c] - 1][c] == 0)
						top[c]--;
				}
			}
		}

		return true;
	}

	private void copyState(State s) {
		for (int i = 0; i < State.ROWS; i++) {
			for (int j = 0; j < State.COLS; j++) {
				field[i][j] = s.getField()[i][j];
			}
		}

		for (int i = 0; i < State.COLS; i++) {
			top[i] = s.getTop()[i];
		}
	}

	public int getRowsCleared() {
		return rowsCleared;
	}

	public int[][] getField() {
		return field;
	}

	public int[] getTop() {
		return top;
	}
}
/* CLUSTER */
package com.cs3243.tetris.cluster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.cs3243.tetris.PlayerSkeleton;
import com.cs3243.tetris.StateStorage;
import com.cs3243.tetris.heuristics.Heuristic;
import com.cs3243.tetris.metaheuristics.Metaheuristic.MetaheuristicTypes;

public class Cluster {

	public String clusterName;
	private String fileName;
	private ArrayList<Heuristic> population;
	private MetaheuristicTypes metaheuristicType;
	private int popSize;
	private PlayerSkeleton ps;
	private StateStorage storage;

	private static final int NUM_GAMES = 1;
	private static final int NUM_GAMES_TEST_BEST = 5; // Test best heuristic over say 5 games

	public Cluster(String clusterName, int popSize, MetaheuristicTypes metaheuristicType)
			throws InstantiationException, IllegalAccessException {
		this.clusterName = clusterName;
		this.fileName = clusterName + ".csv";
		this.popSize = popSize;
		storage = new StateStorage();
		population = new ArrayList<Heuristic>();
		this.metaheuristicType = metaheuristicType;
		if (!storage.readStateFromFile(fileName, population, metaheuristicType))
			initPopulation(metaheuristicType);
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

	private void initPopulation(MetaheuristicTypes metaheuristicType)
			throws InstantiationException, IllegalAccessException {
		Heuristic hs;
		for (int i = 0; i < popSize; i++) {
			hs = Heuristic.heuristicFactory(metaheuristicType);
			population.add(hs);
		}
	}

	public double evaluateFitness() throws InterruptedException {
		double fitnessSum = 0;
		double maxFitness = 0;
		double minFitness = Double.POSITIVE_INFINITY;
		ExecutorService executor = Executors.newFixedThreadPool(popSize);
		
		for (int i = 0; i < popSize; i++) {
			Heuristic hs = population.get(i);
			HeuristicRunner heuristicRunner = new HeuristicRunner();
			heuristicRunner.setHeuristic(hs);
			executor.execute(heuristicRunner);
		}
		
		executor.shutdown();
		executor.awaitTermination(1000, TimeUnit.MINUTES);
		
		for (int i = 0; i < popSize; i++) {
			Heuristic hs = population.get(i);
			double fitness = hs.getFitness();
			
			fitnessSum += fitness;
			if (fitness > maxFitness)
				maxFitness = fitness;
			else if (fitness < minFitness)
				minFitness = fitness;
		}
		
		Collections.sort(population, Collections.reverseOrder());
		Heuristic bestHeuristic = population.get(0);
//		double bestFitness = 0;
//		for (int j = 0; j < NUM_GAMES_TEST_BEST; j++) {
//			bestFitness += ps.playFullGame(bestHeuristic, false);
//		}

		System.out.println("Cluster:" + clusterName);
		System.out.println("Population Statistics");
		System.out.println("Max fitness: " + maxFitness);
		System.out.println("Min fitness: " + minFitness);
		System.out.println("Average fitness: " + fitnessSum / popSize);
//		System.out.println("Best Heuristic, over " + NUM_GAMES_TEST_BEST + " games: " + bestFitness / NUM_GAMES_TEST_BEST);
		System.out.println("Best Heuristic: " + bestHeuristic.toString());
		System.out.println("========================");

		return fitnessSum;
	}

	public void writeStateToFile() {
		storage.writeStateToFile(population, fileName);
	}

	public List<Heuristic> emigrateHeuristics(int numToGet) {
		Collections.sort(population, Collections.reverseOrder());
		return population.subList(0, numToGet).stream().map(heuristic -> heuristic.clone())
				.collect(Collectors.toList());
	}

	public void extraditeWorstHeuristics(int numToRemove) {
		Collections.sort(population);
		population.removeAll(population.subList(0, numToRemove));
	}

	public void immigrateHeuristics(List<Heuristic> heuristics) {
		population.addAll(heuristics.stream().map(heuristic -> Heuristic.heuristicFactory(metaheuristicType, heuristic)).collect(Collectors.toList()));
	}
	
	public class HeuristicRunner implements Runnable {
		private PlayerSkeleton playerSkeleton = new PlayerSkeleton();
		private Heuristic heuristic;
		
		public void setHeuristic(Heuristic heuristic) {
			this.heuristic = heuristic; 
		}
		
		public void run() {
			double fitness = 0;
			
			for (int j = 0; j < NUM_GAMES; j++) {
				fitness += ps.playFullGame(heuristic, false);
			}
			fitness = fitness / NUM_GAMES;
			heuristic.setFitness(fitness);
		}
	}
}
/* HEURISTICS */
package com.cs3243.tetris.heuristics;

import com.cs3243.tetris.features.Feature;

public class GeneticHeuristic extends Heuristic {
	private static final double MUTATION_PROB = 0.1;
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
					feature.getFeatureWeight() * (random.nextGaussian() * MUTATION_STD + MUTATION_MEAN));
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

		// double ft1 = hs1.getFitness();
		// double ft2 = hs2.getFitness();
		// double weightage = (ft1 != 0 || ft2 != 0) ? ft1 / (ft1 + ft2) : 0.5;

		for (int i = 0; i < hs1.getFeatures().length; i++) {
			double hs1Weight = hs1.getFeatures()[i].getFeatureWeight();
			double hs2Weight = hs2.getFeatures()[i].getFeatureWeight();
			// double newWeight = weightage * hs1Weight + (1 - weightage) *
			// hs2Weight;
			// newHeuristics.getFeatures()[i].setFeatureWeight(newWeight);
			newHeuristics.getFeatures()[i].setFeatureWeight(random.nextDouble() < 0.5 ? hs1Weight : hs2Weight);
		}

		return newHeuristics;
	}
}
package com.cs3243.tetris.heuristics;

import java.util.Random;

import com.cs3243.tetris.NextState;
import com.cs3243.tetris.State;
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

	public static Heuristic heuristicFactory(MetaheuristicTypes metaheuristicType) {
		switch (metaheuristicType) {
		case GENETIC:
			return new GeneticHeuristic();
		case PSO:
			return new PSOHeuristic();
		default:
			throw new IllegalArgumentException();
		}
	}
	
	public static Heuristic heuristicFactory(MetaheuristicTypes metaheuristicType, Heuristic heuristic) {
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
package com.cs3243.tetris.heuristics;

import java.util.Arrays;

import com.cs3243.tetris.features.Feature;

public class PSOHeuristic extends Heuristic {
	private final double BOUND_HIGH = -1;
	private final double BOUND_LOW = 1;
	private final double BOUND_RANGE = BOUND_HIGH - BOUND_LOW;

	private double[] personalBestWeights;
	private double personalBestFitness;
	private double[] vel = new double[numFeatures];

	public PSOHeuristic() {
		super();
		personalBestWeights = getWeights(this.features);
		personalBestFitness = 0;
		initVels();
	}

	public PSOHeuristic(Heuristic heuristic) {
		this.features = heuristic.features;
		this.fitness = heuristic.fitness;

		personalBestWeights = getWeights(this.features);
		personalBestFitness = this.fitness;
		initVels();
	}

	private double[] getWeights(Feature[] features) {
		double[] weights = Arrays.stream(features).mapToDouble(feature -> feature.getFeatureWeight()).toArray();
		return weights;
	}

	/**
	 * Initialize velocity: vi ~ U(-BOUND_RANGE, BOUND_RANGE)
	 */
	private void initVels() {
		for (int i = 0; i < numFeatures; i++) {
			vel[i] = random.nextDouble() * BOUND_RANGE * 2 - BOUND_RANGE;
		}
	}

	/**
	 * Update position of particle by adding its velocity
	 */
	public void updatePos() {
		for (int i = 0; i < numFeatures; i++) {
			features[i].setFeatureWeight(features[i].getFeatureWeight() + vel[i]);
		}
	}

	/**
	 * Update a particles velocity taking into account its personal best and the
	 * global best
	 */
	public void updateVel(double omega, double rhop, double rhog, Heuristic globalBest) {
		Feature[] globalBestFeatures = globalBest.features;
		double rp, rg;

		for (int i = 0; i < numFeatures; i++) {
			rp = random.nextDouble();
			rg = random.nextDouble();

			vel[i] = omega * vel[i] + rhop * rp * (personalBestWeights[i] - features[i].getFeatureWeight())
					+ rhog * rg * (globalBestFeatures[i].getFeatureWeight() - features[i].getFeatureWeight());
		}
	}

	@Override
	public void setFitness(double fitness) {
		super.setFitness(fitness);

		if (fitness > this.personalBestFitness) {
			this.personalBestFitness = fitness;
			this.personalBestWeights = getWeights(this.features);
		}
	}

	public double[] getPersonalBestWeights() {
		return this.personalBestWeights;
	}
}
package com.cs3243.tetris;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.cs3243.tetris.features.Feature;
import com.cs3243.tetris.heuristics.Heuristic;
import com.cs3243.tetris.metaheuristics.Metaheuristic.MetaheuristicTypes;

public class StateStorage {
	private BufferedWriter fw;
	private BufferedReader fr;
	
	public void writeStateToFile(ArrayList<Heuristic> population, String fileName){
		try{
			fw = new BufferedWriter(new FileWriter(fileName));
			for(Heuristic hs : population){
				fw.write(hs.toString());
			}
		} catch (Exception e){
			System.out.println("Error in CsvFileWriter!");
			e.printStackTrace();
		} finally{
			try {
				fw.flush();
				fw.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter!");
                e.printStackTrace();
			}
		}
	}
	
	/**
	 * Load saved feature weights from file. 
	 * If file does not exist, create new set of feature weights
	 * 
	 * @param file name
	 * @return ArrayList of Heuristic
	 */
	
	public boolean readStateFromFile(String fileName,ArrayList<Heuristic> population, MetaheuristicTypes metaheuristicType){
		
		boolean fileExist = new File(fileName).isFile();
		if(!fileExist){
			System.out.println("File not found, creating new heuristic sets");
			return false;
		}
		
		String line = "";
		try{
			System.out.println("Loading heuristic from file...");
			fr = new BufferedReader(new FileReader(fileName));
			Feature[] features;
			
			while((line = fr.readLine()) != null){
				String[] tokens = line.split(",");
				Heuristic hs = new Heuristic();
				features = hs.getFeatures();
				
				if(tokens.length == features.length){					
					for (int i = 0; i < features.length; i++){
						double weight = Double.parseDouble(tokens[i]);
						features[i].setFeatureWeight(weight);
					}
					population.add(Heuristic.heuristicFactory(metaheuristicType, hs));
				}
			}
			System.out.println("Number of heuristic is " + population.size());
		} catch(Exception e) {
        	System.out.println("Error in csv FileReader!");
            e.printStackTrace();
		} finally {
			try {
                fr.close();
            } catch (IOException e) {
            	System.out.println("Error while closing fileReader!");
                e.printStackTrace();
            }
		}
		return true;
	}
}
/* ISLANDS */
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
//		c2 = new Island(new GeneticAlgo(), "c2", islandPopulation, MetaheuristicTypes.GENETIC);
		c3 = new Island(new PSOAlgo(),     "c3", islandPopulation, MetaheuristicTypes.PSO);
//		c4 = new Island(new PSOAlgo(),     "c4", islandPopulation, MetaheuristicTypes.PSO);
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
package com.cs3243.tetris.islands;

import java.util.List;

import com.cs3243.tetris.cluster.Cluster;
import com.cs3243.tetris.heuristics.Heuristic;
import com.cs3243.tetris.metaheuristics.Metaheuristic;
import com.cs3243.tetris.metaheuristics.Metaheuristic.MetaheuristicTypes;

public class Island implements Runnable {
	private Metaheuristic metaheuristic;
	private Cluster cluster;

	public Island(Metaheuristic metaheuristic, String clusterName, int populationSize,
			      MetaheuristicTypes metaheuristicType) throws InstantiationException, IllegalAccessException {
		this.cluster = new Cluster(clusterName, populationSize, metaheuristicType);
		this.metaheuristic = metaheuristic;
		this.metaheuristic.setCluster(cluster);
	}

	public void runOneGen() throws InterruptedException {
		metaheuristic.createNextGen();
		metaheuristic.getCluster().writeStateToFile();
	}
	
	public void exchangeHeuristics(Island island, int numToExchange) {
		List<Heuristic> thisBestHeuristics  = this.cluster.emigrateHeuristics(numToExchange);
		List<Heuristic> otherBestHeuristics = island.cluster.emigrateHeuristics(numToExchange);
		
		this.cluster.extraditeWorstHeuristics(numToExchange);
		island.cluster.extraditeWorstHeuristics(numToExchange);
		
		this.cluster.immigrateHeuristics(otherBestHeuristics);
		island.cluster.immigrateHeuristics(thisBestHeuristics);
	}

	public void run() {
		try {
			runOneGen();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
/* METAHEURISTICS */
package com.cs3243.tetris.metaheuristics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.cs3243.tetris.heuristics.GeneticHeuristic;
import com.cs3243.tetris.heuristics.Heuristic;

public class GeneticAlgo extends Metaheuristic {
	double FRAC_PARENTS_KEPT = 0.5;

	@Override
	/*
	 * Keep the top few percent of the population The rest go through
	 * recombination by roulette wheel selection
	 */
	public void createNextGen() throws InterruptedException {
		if (cluster == null)
			return;

		int popSize = cluster.getPopSize();
		ArrayList<Heuristic> population = cluster.getPopulation();
		double fitnessSum = cluster.evaluateFitness();

		int numKept = (int) (popSize * FRAC_PARENTS_KEPT);
		int numCrossOver = popSize - numKept;

		ArrayList<Heuristic> newPopulation = new ArrayList<Heuristic>();
		Collections.sort(population, Collections.reverseOrder());
		newPopulation.addAll(population.subList(0, numKept));

		Heuristic parent1 = null, parent2 = null;

		for (int i = 0; i < numCrossOver; i++) {
			for (int j = 0; j < 2; j++) {
				double partialSum = 0;
				double cutoff = rand.nextDouble() * fitnessSum;
				int k = 0;

				// If rand.nextDouble is so close to 1 that cutoff rounds
				// off to fitnessSum, k will become popSize, hence the k <
				// popSize check
				while (partialSum <= cutoff & k < popSize) {
					partialSum += population.get(k).getFitness();
					k++;
				}

				if (j == 0) {
					parent1 = population.get(k - 1);
				} else {
					parent2 = population.get(k - 1);
				}
			}

			GeneticHeuristic child = GeneticHeuristic.crossover((GeneticHeuristic) parent1, (GeneticHeuristic) parent2);
			child.mutate();
			newPopulation.add(child);
		}

		cluster.updatePopulation(newPopulation);
	}

//	@Override
//	public List<Heuristic> emigrateHeuristics(int numToGet) {
//		return cluster.emigrateHeuristics(numToGet);
//	}
//
//	@Override
//	public void extraditeWorstHeuristics(int numToRemove) {
//		cluster.extraditeWorstHeuristics(numToRemove);
//	}
//
//	@Override
//	public void immigrateHeuristics(List<Heuristic> heuristics) {
//		cluster.immigrateHeuristics(heuristics, MetaheuristicTypes.GENETIC);
//	}
}
package com.cs3243.tetris.metaheuristics;

import java.util.List;
import java.util.Random;

import com.cs3243.tetris.cluster.Cluster;
import com.cs3243.tetris.heuristics.Heuristic;

public abstract class Metaheuristic {
	protected Random rand = new Random();
	protected Cluster cluster = null;

	public enum MetaheuristicTypes {
		GENETIC, PSO
	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	public Cluster getCluster() {
		return cluster;
	}

	abstract public void createNextGen() throws InterruptedException;
//	abstract public List<Heuristic> emigrateHeuristics(int numToGet);
//	abstract public void extraditeWorstHeuristics(int numToRemove);
//	abstract public void immigrateHeuristics(List<Heuristic> heuristics);
}
package com.cs3243.tetris.metaheuristics;

import java.util.ArrayList;
import java.util.List;

import com.cs3243.tetris.cluster.Cluster;
import com.cs3243.tetris.heuristics.Heuristic;
import com.cs3243.tetris.heuristics.PSOHeuristic;

public class PSOAlgo extends Metaheuristic {
	private final double OMEGA = 0.6;
	private final double RHOP = 1.4;
	private final double RHOG = 2.0;
	
	Heuristic globalBest;
	ArrayList<Heuristic> population;
	
	int popSize;
	int numFeatures;
	
	@Override
	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
		
		population = this.cluster.getPopulation();
		popSize = this.cluster.getPopSize();
		numFeatures = this.cluster.getPopulation().get(0).getNumFeatures();
		globalBest = new Heuristic();
		
		// Cannot evaluate fitness here because setCluster is called during instantiation of Island and it will delay the main thread
		// cluster.evaluateFitness(); 
		// initPositions();
	}
	
	/**
	 * Initialize personal bests with current fitness for each heuristic
	 * Update global best to be best over all the current personal bests 
	 */
	private void initPositions() {
		globalBest.setFitness(0);
		updateGlobalBest();
	}
	
	/**
	 * Update global best
	 */
	private void updateGlobalBest() {
		for (Heuristic heuristic : population) {
			if (heuristic.getFitness() > globalBest.getFitness()) {
				globalBest = heuristic.clone();
			}
		}
	}
	
	@Override
	public void createNextGen() throws InterruptedException {
		PSOHeuristic psoHeuristic;
		
		// Evaluate fitness and initialize positions if first generation
		if (globalBest == null) {
			 cluster.evaluateFitness(); 
			 initPositions();
		}
		
		// Move every particle
		for (Heuristic heuristic : population) {
			psoHeuristic = ((PSOHeuristic) heuristic);
			psoHeuristic.updateVel(OMEGA, RHOP, RHOG, globalBest);
			psoHeuristic.updatePos();
		}
		
		cluster.evaluateFitness();
		
		updateGlobalBest();
	}

//	@Override
//	public List<Heuristic> emigrateHeuristics(int numToGet) {
//		return cluster.emigrateHeuristics(numToGet);
//	}
//
//	@Override
//	public void extraditeWorstHeuristics(int numToRemove) {
//		cluster.extraditeWorstHeuristics(numToRemove);
//	}
//
//	@Override
//	public void immigrateHeuristics(List<Heuristic> heuristics) {
//		cluster.immigrateHeuristics(heuristics, MetaheuristicTypes.PSO);
//		updateGlobalBest();
//	}
}
/* FEATURES */
package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

public class AltitudeDiff extends Feature {
    
    private int minHeight = Integer.MAX_VALUE;
    private int maxHeight = Integer.MIN_VALUE;
	
    @Override
    public double getScore() {
        return featureWeight * (maxHeight - minHeight);
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        int[] top = s.getTop();
        if(top[col] > maxHeight) maxHeight = top[col];
        else if(top[col] < minHeight) minHeight = top[col];
        
    }

    @Override
    public void resetScore() {
        minHeight = Integer.MAX_VALUE;
        maxHeight = Integer.MIN_VALUE;      
    }
}
package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

/*
 * Counting unoccupied -> occupied and occupied -> unoccupied transition within a column
 */

public class ColTransition extends Feature {
    
    private int count = 0;

    @Override
    public double getScore() {
        return featureWeight * count;
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        int[][] field = s.getField();
        if(row == 0 && field[row][col] == 0) count ++;
        if(field[row][col] == 0 && field[row+1][col] != 0) count ++;
        else if(field[row][col] != 0 && field[row+1][col] == 0) count ++;
        
    }

    @Override
    public void resetScore() {
        count = 0;        
    }

}
package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

/**
 * This heuristic calculates the number of columns with holes.
 */
public class ColWithHole extends Feature {
    private int count = 0;
    private boolean[] hasHole;
    
    public ColWithHole() {
        hasHole = null;
    }
    
    public ColWithHole(int numCols) {
        hasHole = new boolean[numCols];
    }

    @Override
    public double getScore() {
        return featureWeight * count;
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        int[][] field = s.getField();
        if (!hasHole[col]) {
            if (field[row][col] == 0) {
                hasHole[col] = true;
                count++;
            }
        }        
    }

    @Override
    public void resetScore() {
        count = 0;
    }
}
package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

/**
 * This heuristic calculates the height of the lowest column.
 */
public class DeepestWell extends Feature {
    
    private int maxWellDepth = 0;
  
    @Override
    public double getScore() {
        return featureWeight * maxWellDepth;
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        int[] top = s.getTop();
        int wellDepth = 0;
        if (col == 0 && top[col + 1] > top[col]) {
            wellDepth = top[col + 1] - top[col];
        } else if (col == top.length - 1 && top[col - 1] > top[col]) {
            wellDepth = top[col - 1] - top[col];
        } else if (col != 0 && col != top.length - 1 && top[col - 1] > top[col] && top[col + 1] > top[col]) {
            wellDepth = Math.min(top[col - 1], top[col + 1]) - top[col]; 
        }

        maxWellDepth = Math.max(maxWellDepth, wellDepth);
    }

    @Override
    public void resetScore() {
       maxWellDepth = 0;
        
    }
}
package com.cs3243.tetris.features;

import java.util.Random;

import com.cs3243.tetris.NextState;
import com.cs3243.tetris.State;

public abstract class Feature {
	protected double featureWeight;
	private Random rand = new Random();

	/**
	 * Initialize feature with random weight
	 */
	public Feature() {
		featureWeight = 2 * 100 * rand.nextDouble() - 100;
	}

	/**
	 * Get feature weight
	 * 
	 * @param weight
	 * @return
	 */
	public double getFeatureWeight() {
		return featureWeight;
	}

	/**
	 * Set feature weight
	 * 
	 * @param weight
	 */
	public void setFeatureWeight(double weight) {
		featureWeight = weight;
	}
	
	public Feature clone() {
		try {
			Feature cloneFeature = this.getClass().newInstance();
			cloneFeature.setFeatureWeight(this.featureWeight);
			return cloneFeature;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Calculate score of feature. Should be overridden with proper
	 * implementation of scoring 
	 * @param s
	 * @return double score
	 */
	public abstract double getScore();
	
	/**
     * Updates score of feature. Should be overridden with proper
     * implementation of scoring 
     * @param s, row, col
     */
	public abstract void updateScore(NextState s, int row, int col);
	
	/**
     * Updates score of feature. Should be overridden with proper
     * implementation of scoring 
     * @param s, row, col
     */
	public void updateScore(State s, NextState ns, int row, int col) {
		throw new UnsupportedOperationException();
	}
	
	/**
     * Resets. Should be overridden with proper
     * implementation of scoring
     */
	public abstract void resetScore();
}
package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

/**
 * This heuristic calculates the height of the highest column.
 */
public class HighestCol extends Feature {
    private int maxCol = 0;

    @Override
    public double getScore() {
        return featureWeight * maxCol;
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        int[] top = s.getTop();
        maxCol = Math.max(maxCol, top[col]);
        
    }

    @Override
    public void resetScore() {
        maxCol = 0;
        
    }
}
package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

/**
 * This heuristic calculates the number of holes. A column has a hole if it is
 * lower than the highest column.
 */
public class NumHoles extends Feature {
    private int holes = 0;

    @Override
    public double getScore() {
        return featureWeight * holes;
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        int[][] field = s.getField();
        if (field[row][col] == 0) {
            holes++;
        }
        
    }

    @Override
    public void resetScore() {
        holes = 0;     
    }
}
package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

/**
 * This heuristic calculates the number of wells. A well is defined as a column
 * that is lower than its adjacent columns.
 */
public class NumWells extends Feature {
    private int count = 0;

    @Override
    public double getScore() {
        return featureWeight * count;
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        int[] top = s.getTop();
        if (col == 0 && top[col + 1] > top[col]) {
            count++;
        } else if (col == top.length - 1 && top[col - 1] > top[col]) {
            count++;
        } else if (col != 0 && col != top.length - 1 && top[col - 1] > top[col] && top[col + 1] > top[col]) {
            count++;
        }
        
    }

    @Override
    public void resetScore() {
        count = 0;
        
    }
}
package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

/*
 * Counting unoccupied -> occupied and occupied -> unoccupied transition within a row
 */

public class RowTransition extends Feature{
	private int count = 0;
	
    @Override
    public double getScore() {
        return featureWeight * count;
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        int[][] field = s.getField();
        int cols = field[0].length;
        
        if(col == 0 && field[row][col] == 0) count ++;
        else if(col == cols - 1 && field[row][col] == 0) count ++;
        
        if(col != cols -1){
            if(field[row][col] == 0 && field[row][col+1] != 0) count ++;
            else if(field[row][col] != 0 && field[row][col+1] == 0) count ++;
        }
        
    }

    @Override
    public void resetScore() {
        count = 0;
        
    }

}
package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;
import com.cs3243.tetris.State;

/**
 * This heuristic calculates the number of rows cleared.
 */
public class RowsCleared extends Feature {
    private double rowsCleared = 0;

    @Override
    public double getScore() {
        return featureWeight * rowsCleared;
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        throw new UnsupportedOperationException(); 
    }
    
    @Override
    public void updateScore(State s, NextState ns, int row, int col) {
    	rowsCleared = ns.getRowsCleared() - s.getRowsCleared();
    };

    @Override
    public void resetScore() {
       rowsCleared = 0;
        
    }
}
package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

/**
 * This heuristic calculates the total height of all columns.
 */
public class TotalColHeight extends Feature {
    
    private int sum = 0;

    @Override
    public double getScore() {
        return featureWeight * sum;
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        int[] top = s.getTop();
        sum += top[col];
        
    }

    @Override
    public void resetScore() {
        sum = 0;
        
    }
}
package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

/**
 * This heuristic calculates the total height difference between adjacent
 * columns.
 */
public class TotalColHeightDiff extends Feature {
    private int sum = 0;

    @Override
    public double getScore() {
        return featureWeight * sum;
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        int[] top = s.getTop();
        if (col != top.length - 1) {
            sum += Math.abs(top[col + 1] - top[col]);
        }
        
    }

    @Override
    public void resetScore() {
        sum = 0;
        
    }
}
package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

public class WeightedBlock extends Feature {
    private int sum = 0;
    
    @Override
    public double getScore() {
        return featureWeight * sum;
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        int[][] field = s.getField();
        if(field[row][col] != 0) sum += row+1;
        
    }

    @Override
    public void resetScore() {
        sum = 0;
    }
}
package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

public class WellSum extends Feature{
	private int wellSum = 0;
	
    @Override
    public double getScore() {
        return featureWeight * wellSum ;
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        int[] top = s.getTop();

        if (col == 0 || col == top.length - 1) {
            if(col == 0 && top[col + 1] > top[col]){
                wellSum += top[col+1] - top[col];
            } else if(col == top.length -1 && top[col-1] > top[col]){
                wellSum += top[col-1] - top[col];
            }
        } else if (top[col - 1] > top[col] && top[col + 1] > top[col]) {
            wellSum += Math.min(top[col-1], top[col+1]) - top[col]; 
        }
        
    }

    @Override
    public void resetScore() {
        wellSum = 0;
        
    }

}
