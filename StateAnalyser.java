
public class StateAnalyser {

	private int[] weight = new int[22];
	private static final int OFFSET_COLUMN_HEIGHT = 1;
	private static final int OFFSET_COLUMN_DIFF = 11;
	private static final int OFFSET_MAX_COLUMN_HEIGHT = 20;
	private static final int OFFSET_NUM_HOLES = 21;
	
	public StateAnalyser(){
		for(int i=0; i<weight.length; i++){
			weight[i] = 1;
		}
	}
	
	public int calculateHeuristic(int[][] field, int[] top){
		int score = weight[0];
		int maxHeight = 0;
		
		for(int i=0; i<top.length; i++){
			score += weight[i+OFFSET_COLUMN_HEIGHT] * top[i];
			if(top[i] > maxHeight) maxHeight = top[i];
			if(i != top.length-1){
				score += weight[i+OFFSET_COLUMN_DIFF] * Math.abs(top[i+1]-top[i]); 
			}
		}
		
		score += weight[OFFSET_MAX_COLUMN_HEIGHT] * maxHeight;
		
		int numHoles = 0;
		
//		for(int i=0; i<field.length; i++){
//			for(int j=0; j<field[0].length; j++){
//				boolean isHole = true;
//				if(field[i][j]==0){
//					//check four sides
//					if(j-1 >=0 && field[i][j-1] == 0) isHole = false;
//					else if(j+1 < top.length && field[i][j+1] == 0) isHole = false;
//					else if(i-1 >=0 && field[i-1][j] == 0) isHole = false;
//					else if(i+1 < field.length && field[i+1][j] == 0) isHole = false;
//					
//					if(isHole) numHoles++;
//				}
//			}
//		}
		
		score += weight[OFFSET_NUM_HOLES] * numHoles;
		
		
		return score;
	}
}
