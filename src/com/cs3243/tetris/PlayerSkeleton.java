package com.cs3243.tetris;

import com.cs3243.tetris.heuristics.Heuristic;

/**
 * Player Skeleton Methods for playing game
 *
 * @author Advay Pal, Chang Chu-Ming, Gao Bo, Herbert Ilhan Tanujaya, Varun
 *         Gupta
 */
public class PlayerSkeleton {

	// implement this function to have a working system
	public int pickMove(NextState s, int[][] legalMoves, NextState ns, Heuristic hs) {
		double maxScore = Double.NEGATIVE_INFINITY;
		int maxIndex = 0;

		for (int i = 0; i < legalMoves.length; i++) {
			boolean notLost = ns.generateNextState(s, legalMoves[i]);
			if (!notLost) {
				continue;
			}

			double score = hs.calculateHeuristicScore(ns);

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
		NextState s = new NextState();
		NextState ns = new NextState();
		// Heuristics hs = new Heuristics();
//		if (graphic) {
//			new TFrame(s);
//		}

		PlayerSkeleton p = new PlayerSkeleton();
		while (!s.hasLost()) {
			s.makeMove(p.pickMove(s, s.legalMoves(), ns, hs));
		}
		return s.getRowsCleared();
	}

	public static void main(String[] args) {
		NextState s = new NextState();
		NextState ns = new NextState();
		Heuristic hs = new Heuristic();
		hs.features[0].setFeatureWeight(-93.02173123251356);
		hs.features[1].setFeatureWeight(-308.24909375997737);
		hs.features[2].setFeatureWeight(-569.9966890295316);
		hs.features[3].setFeatureWeight(-0.5566053775002548);
		hs.features[4].setFeatureWeight(459.7334110849129);
		hs.features[5].setFeatureWeight(-80.920643335677);
		hs.features[6].setFeatureWeight(-25.073988080939912);
		hs.features[7].setFeatureWeight(-562.3724669171518);
		hs.features[8].setFeatureWeight(-691.1628729179465);
		hs.features[9].setFeatureWeight(-2691.7035043803835);
		hs.features[10].setFeatureWeight(80.98441874131163);
		hs.features[11].setFeatureWeight(-723.3118170031345);
		hs.features[12].setFeatureWeight(-8.869080880327157);
//		new TFrame(s);
		PlayerSkeleton p = new PlayerSkeleton();
		while (!s.hasLost()) {
			s.makeMove(p.pickMove(s, s.legalMoves(), ns, hs));
//			s.draw();
//			s.drawNext(0, 0);
//			try {
//				Thread.sleep(300);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
		System.out.println("You have completed " + s.getRowsCleared() + " rows.");
	}

}
