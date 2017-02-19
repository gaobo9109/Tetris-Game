
public class StateAnalyser {

	private int[] weight = new int[22];
	private static final int OFFSET_COLUMN_HEIGHT = 1;
	private static final int OFFSET_COLUMN_DIFF = 11;
	private static final int OFFSET_MAX_COLUMN_HEIGHT = 20;
	private static final int OFFSET_NUM_HOLES = 21;
	
	private int[][] field = new int[State.ROWS][State.COLS];
	private int[] top = new int[State.COLS];
	
	public StateAnalyser(){
		for(int i=0; i<weight.length; i++){
			weight[i] = 1;
		}
	}
	
	public int genNextStateFromMove(State s, int[] legalMoves){
		int orient = legalMoves[0];
		int slot = legalMoves[1];
		int nextPiece = s.getNextPiece();
		int[][][] pBottom = State.getpBottom();
		int[][] pHeight = State.getpHeight();
		int[][] pWidth = State.getpWidth();
		int[][][] pTop = State.getpTop();
		
		copyState(s);
		
		//height if the first column makes contact
		int height = top[slot]-pBottom[nextPiece][orient][0];
		//for each column beyond the first in the piece
		for(int c = 1; c < pWidth[nextPiece][orient];c++) {
			height = Math.max(height,top[slot+c]-pBottom[nextPiece][orient][c]);
		}
		
		//check if game ended
		if(height+pHeight[nextPiece][orient] >= State.ROWS) {
			return 0;
		}

		
		//for each column in the piece - fill in the appropriate blocks
		for(int i = 0; i < pWidth[nextPiece][orient]; i++) {
			
			//from bottom to top of brick
			for(int h = height+pBottom[nextPiece][orient][i]; h < height+pTop[nextPiece][orient][i]; h++) {
				field[h][i+slot] = 1;
			}
		}
		
		//adjust top
		for(int c = 0; c < pWidth[nextPiece][orient]; c++) {
			top[slot+c]=height+pTop[nextPiece][orient][c];
		}
		
		int rowsCleared = 0;
		
		//check for full rows - starting at the top
		for(int r = height+pHeight[nextPiece][orient]-1; r >= height; r--) {
			//check all columns in the row
			boolean full = true;
			for(int c = 0; c < State.COLS; c++) {
				if(field[r][c] == 0) {
					full = false;
					break;
				}
			}
			//if the row was full - remove it and slide above stuff down
			if(full) {
				rowsCleared++;
				//for each column
				for(int c = 0; c < State.COLS; c++) {

					//slide down all bricks
					for(int i = r; i < top[c]; i++) {
						field[i][c] = field[i+1][c];
					}
					//lower the top
					top[c]--;
					while(top[c]>=1 && field[top[c]-1][c]==0)	top[c]--;
				}
			}
		}
		
		return rowsCleared;
	}
	
	private void copyState(State s){

		
		for(int i=0; i<State.ROWS; i++){
			for(int j=0; j<State.COLS; j++){
				field[i][j] = s.getField()[i][j];
			}
		}
		
		for(int i=0; i<State.COLS; i++){
			top[i] = s.getTop()[i];
		}
	}
	
	public int calculateHeuristic(){
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
