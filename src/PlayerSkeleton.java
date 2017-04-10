import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
//		if (graphic) {
//			new TFrame(s);
//		}

		PlayerSkeleton p = new PlayerSkeleton();
		while (!s.hasLost()) {
			s.makeMove(p.pickMove(s, s.legalMoves(), ns, hs));
		}
		return s.getRowsCleared();
	}

	public static void main(String[] args) throws IOException {
		BufferedReader fr = new BufferedReader(new FileReader(args[0]));
		
		State s = new State();
		NextState ns = new NextState();
		Heuristic hs = new Heuristic();
		
		String line = fr.readLine();
		String[] tokens = line.split(",");
		
		Feature[] features = hs.getFeatures();
		
		for (int i = 0; i < features.length; i++){
			double weight = Double.parseDouble(tokens[i]);
			features[i].setFeatureWeight(weight);
		}
		
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
