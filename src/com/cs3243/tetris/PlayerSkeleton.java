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
