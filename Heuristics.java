
public class Heuristics {
	
	private int[] featureWeight = new int[8]; 
	
	public Heuristics(){
		//TODO generate feature weight
	}
	
	private int totalColHeight(int[] top){
		int sum = 0;
		for(int i:top){
			sum += i;
		}
		return sum;
	}
	
	private int totalColHeightDiff(int[] top){
		int sum = 0;
		for(int i=0; i<top.length-1; i++){
			sum += Math.abs(top[i+1] - top[i]);
		}
		return sum;
	}
	
	private int highestCol(int[] top){
		int maxCol = 0;
		for(int i : top){
			maxCol = Math.max(maxCol, top[i]);
		}
		return maxCol;
	}
	
	private int numWells(int[] top){
		int count = 0;
		for(int i=0; i<top.length; i++){
			if(i==0 && top[i+1] > top[i]) count ++;
			else if(i==top.length-1 && top[i-1] > top[i]) count ++;
			else if(top[i-1] > top[i] && top[i+1] > top[i]) count ++;
			
		}
		return count;
	}
	
	private int deepestWell(int[] top){
		int minWellRow = top[0];
		
		for(int i=0; i<top.length; i++){
			int wellRow = 0;
			
			if(i==0 && top[i+1] > top[i]) wellRow = top[i];
			else if(i==top.length-1 && top[i-1] > top[i]) wellRow = top[i];
			else if(top[i-1] > top[i] && top[i+1] > top[i]) wellRow = top[i];
			
			minWellRow = Math.min(minWellRow, wellRow);
			
		}
		return minWellRow;
	}
	
	private int numHoles(int[][] field, int[] top){
		int holes = 0;
		for(int i=0; i<top.length; i++){
			int colHeight = top[i];
			for(int j=0; j<colHeight-1; j++){
				//any empty cell beneath the top row cell in that col is a hole 
				if(field[j][i]==0) holes++;
			}
		}
		return holes;
	}
	
	private int colWithHole(int[][] field, int[] top){
		int count = 0;
		for(int i=0; i<top.length; i++){
			int colHeight = top[i];
			for(int j=0; j<colHeight-1; j++){
				if(field[j][i]==0){
					count ++;
					break;
				}
			}
		}
		return count;
	}
	
	public int calculateHeuristicScore(NextState s){
		int[][] field = s.getField();
		int[] top = s.getTop();
		int score = 0;
		
		score += featureWeight[0] * s.getRowsCleared();
		score += featureWeight[1] * totalColHeight(top);
		score += featureWeight[2] * totalColHeightDiff(top);
		score += featureWeight[3] * highestCol(top);
		score += featureWeight[4] * numWells(top);
		score += featureWeight[5] * deepestWell(top);
		score += featureWeight[6] * numHoles(field,top);
		score += featureWeight[7] * colWithHole(field,top);
		
		return score;
	}
	
}
