package com.cs3243.tetris;

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

	public static void main(String[] args) {
		State s = new State();
		NextState ns = new NextState();
		Heuristic hs = new Heuristic();
		hs.getFeatures()[0].setFeatureWeight(128.43563440404822);
		hs.getFeatures()[1].setFeatureWeight(-126.17471819457865);
		hs.getFeatures()[2].setFeatureWeight(-319.47774996317463);
		hs.getFeatures()[3].setFeatureWeight(-179.67090757404947);
		hs.getFeatures()[4].setFeatureWeight(230.45084173101048);
		hs.getFeatures()[5].setFeatureWeight(-44.940227217941256);
		hs.getFeatures()[6].setFeatureWeight(-6.042610515968329);
		hs.getFeatures()[7].setFeatureWeight(-645.0241688995694);
		hs.getFeatures()[8].setFeatureWeight(-1612.6604575058468);
		hs.getFeatures()[9].setFeatureWeight(-1313.3709190648046);
		hs.getFeatures()[10].setFeatureWeight(-35.786051997070814);
		hs.getFeatures()[11].setFeatureWeight(-518.9406113442949);
		hs.getFeatures()[12].setFeatureWeight(-6.875086949880808);
		
//		new TFrame(s);
		PlayerSkeleton p = new PlayerSkeleton();
		int i = 0;
		while (!s.hasLost()) {
			s.makeMove(p.pickMove(s, s.legalMoves(), ns, hs));
			if (s.getRowsCleared() % 10000 == 0) {
				System.out.println(s.getRowsCleared());
			}
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
