

public class PlayerSkeleton {

	//implement this function to have a working system
	public int pickMove(State s, int[][] legalMoves, NextState ns, Heuristics hs) {
		int maxScore = 99999;
		int maxIndex = 0;
		
		for(int i=0; i<legalMoves.length; i++){
			boolean notLost = ns.genNextState(s,legalMoves[i]);
			if(!notLost) continue;
			
			int score = hs.calculateHeuristicScore(ns);
			
			if(score < maxScore){
				maxScore = score;
				maxIndex = i;
			}

		}
		
		return maxIndex;
	}
	
	public double playFullGame(Heuristics hs, boolean graphic){
		State s = new State();
		NextState ns = new NextState();
//		Heuristics hs = new Heuristics();
		if(graphic) {
			new TFrame(s);
		}
		
		PlayerSkeleton p = new PlayerSkeleton();
		while(!s.hasLost()) {
			s.makeMove(p.pickMove(s,s.legalMoves(),ns,hs));
			if(graphic){
				s.draw();
				s.drawNext(0,0);
			}
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		return s.getRowsCleared();
	}
	
	public static void main(String[] args) {
		State s = new State();
		NextState ns = new NextState();
		Heuristics hs = new Heuristics();
		new TFrame(s);
		PlayerSkeleton p = new PlayerSkeleton();
		while(!s.hasLost()) {
			s.makeMove(p.pickMove(s,s.legalMoves(),ns,hs));
			s.draw();
			s.drawNext(0,0);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		System.out.println("You have completed "+s.getRowsCleared()+" rows.");
	}
	
}
