package com.cs3243.tetris;

import java.awt.Color;
import java.util.Arrays;
import java.util.Random;

/**
 * Generates next state of the game. Useful for calculating score of next state
 * 
 * @author Advay Pal, Chang Chu-Ming, Gao Bo, Herbert Ilhan Tanujaya, Varun
 *         Gupta
 */

public class NextState {
	public int[][] field = new int[State.ROWS][State.COLS];
	private int[] top = new int[State.COLS];
	private static int rowsCleared = 0;

	private int[][][] pBottom = State.getpBottom();
	private int[][] pHeight = State.getpHeight();
	private int[][] pWidth = State.getpWidth();
	private int[][][] pTop = State.getpTop();

	public boolean generateNextState(NextState s, int[] legalMoves) {
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
//				System.out.println(height + pBottom[nextPiece][orient][i]);
//				System.out.println();
				field[h][i + slot] = 1;
			}
		}

		// adjust top
		for (int c = 0; c < pWidth[nextPiece][orient]; c++) {
			top[slot + c] = height + pTop[nextPiece][orient][c];
		}

//		rowsCleared = 0;

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
				cleared++;

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

	public void copyState(NextState s) {
		for (int i = 0; i < NextState.ROWS; i++) {
			for (int j = 0; j < NextState.COLS; j++) {
				field[i][j] = s.getField()[i][j];
			}
		}

		for (int i = 0; i < NextState.COLS; i++) {
			top[i] = s.getTop()[i];
		}
	}

	public int getRowsCleared() {
		return cleared;
	}
	
	public static final int COLS = 10;
	public static final int ROWS = 21;
	public static final int N_PIECES = 7;

	

	public boolean lost = false;
	
	
	
	public TLabel label;
	
	//current turn
	private int turn = 0;
	private int cleared = 0;
	
	//each square in the grid - int means empty - other values mean the turn it was placed
	//top row+1 of each column
	//0 means empty
	
	
	//number of next piece
	public int nextPiece;
	
	
	
	//all legal moves - first index is piece type - then a list of 2-length arrays
	protected static int[][][] legalMoves = new int[N_PIECES][][];
	
	//indices for legalMoves
	public static final int ORIENT = 0;
	public static final int SLOT = 1;
	
	//possible orientations for a given piece type
	protected static int[] pOrients = {1,2,4,4,4,2,2};
	
	
	//initialize legalMoves
	{
		//for each piece type
		for(int i = 0; i < N_PIECES; i++) {
			//figure number of legal moves
			int n = 0;
			for(int j = 0; j < pOrients[i]; j++) {
				//number of locations in this orientation
				n += COLS+1-pWidth[i][j];
			}
			//allocate space
			legalMoves[i] = new int[n][2];
			//for each orientation
			n = 0;
			for(int j = 0; j < pOrients[i]; j++) {
				//for each slot
				for(int k = 0; k < COLS+1-pWidth[i][j];k++) {
					legalMoves[i][n][ORIENT] = j;
					legalMoves[i][n][SLOT] = k;
					n++;
				}
			}
		}
	
	}
	
	
	public int[][] getField() {
		return field;
	}

	public int[] getTop() {
		return top;
	}

    public static int[] getpOrients() {
        return pOrients;
    }


	public int getNextPiece() {
		return nextPiece;
	}
	
	public boolean hasLost() {
		return lost;
	}
	
	public int getTurnNumber() {
		return turn;
	}
	
	
	
	//constructor
	public NextState() {
		nextPiece = randomPiece();

	}
	
	public static NextState generateStateWithOneHole() {
		NextState state = new NextState();
		for (int c = 1; c < COLS; c++) {
			for (int r = 0; r < 4; r++) {
				state.field[r][c] = 1;
			}
		}
		
		for (int c = 0; c < COLS; c++) {
			for (int r = ROWS - 2; r >= 0; r--) {
				if (state.field[r][c] == 1) {
					state.top[c] = r + 1;
					break;
				}
			}
		}
		
		state.nextPiece = 1;
		
		return state;
	}
	
	public static NextState generateRandomState() {
		NextState state = new NextState();
		Random random = new Random();
		
//		while (!isValid) {
		for (int c = 0; c < COLS; c++) {
			for (int r = 0; r < ROWS; r++) {
				state.field[r][c] = random.nextInt(2);
			}
		}
		
		for (int c = 0; c < COLS; c++) {
			for (int r = ROWS - 2; r >= 0; r--) {
				if (state.field[r][c] == 1) {
					state.top[c] = r + 1;
					break;
				}
			}
		}
			
//			isValid = true;
//			
//			for (int r = 0; r < ROWS; r++) {
//				boolean has1 = false;
//				boolean has0 = false;
//				
//				for (int c = 0; c < COLS; c++) {
//					if (state.field[r][c] == 1) has1 = true;
//					if (state.field[r][c] == 0) has0 = true;
//				}
//				
//				if (!has0 || !has1) isValid = false;  
//			}
//		}
		
		return state;
	}
	
	//random integer, returns 0-6
	private int randomPiece() {
		return (int)(Math.random()*N_PIECES);
	}
	


	
	//gives legal moves for 
	public int[][] legalMoves() {
		return legalMoves[nextPiece];
	}
	
	//make a move based on the move index - its order in the legalMoves list
	public void makeMove(int move) {
		makeMove(legalMoves[nextPiece][move]);
	}
	
	//make a move based on an array of orient and slot
	public void makeMove(int[] move) {
		makeMove(move[ORIENT],move[SLOT]);
	}
	
	
	//returns false if you lose - true otherwise
	public boolean makeMove(int orient, int slot) {
		turn++;
		
		//height if the first column makes contact
		int height = top[slot]-pBottom[nextPiece][orient][0];
		//for each column beyond the first in the piece
		for(int c = 1; c < pWidth[nextPiece][orient];c++) {
			height = Math.max(height,top[slot+c]-pBottom[nextPiece][orient][c]);
		}
		
		//check if game ended
		if(height+pHeight[nextPiece][orient] >= ROWS) {
			lost = true;
			return false;
		}

		
		//for each column in the piece - fill in the appropriate blocks
		for(int i = 0; i < pWidth[nextPiece][orient]; i++) {
			
			//from bottom to top of brick
			for(int h = height+pBottom[nextPiece][orient][i]; h < height+pTop[nextPiece][orient][i]; h++) {
				field[h][i+slot] = turn;
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
			for(int c = 0; c < COLS; c++) {
				if(field[r][c] == 0) {
					full = false;
					break;
				}
			}
			//if the row was full - remove it and slide above stuff down
			if(full) {
				rowsCleared++;
				cleared++;
				//for each column
				for(int c = 0; c < COLS; c++) {

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
	

		//pick a new piece
		nextPiece = randomPiece();
			
		return true;
	}
	
	
	public void draw() {
		label.clear();
		label.setPenRadius();
		//outline board
		label.line(0, 0, 0, ROWS+5);
		label.line(COLS, 0, COLS, ROWS+5);
		label.line(0, 0, COLS, 0);
		label.line(0, ROWS-1, COLS, ROWS-1);
		
		//show bricks
				
		for(int c = 0; c < COLS; c++) {
			for(int r = 0; r < top[c]; r++) {
				if(field[r][c] != 0) {
					drawBrick(c,r);
				}
			}
		}
		
		for(int i = 0; i < COLS; i++) {
			label.setPenColor(Color.red);
			label.line(i, top[i], i+1, top[i]);
			label.setPenColor();
		}
		
		label.show();
		
		
	}
	
	public static final Color brickCol = Color.gray; 
	
	private void drawBrick(int c, int r) {
		label.filledRectangleLL(c, r, 1, 1, brickCol);
		label.rectangleLL(c, r, 1, 1);
	}
	
	
	public void drawNext(int slot, int orient) {
		for(int i = 0; i < pWidth[nextPiece][orient]; i++) {
			for(int j = pBottom[nextPiece][orient][i]; j <pTop[nextPiece][orient][i]; j++) {
				drawBrick(i+slot, j+ROWS+1);
			}
		}
		label.show();
	}
	
	//visualization
	//clears the area where the next piece is shown (top)
	public void clearNext() {
		label.filledRectangleLL(0, ROWS+.9, COLS, 4.2, TLabel.DEFAULT_CLEAR_COLOR);
		label.line(0, 0, 0, ROWS+5);
		label.line(COLS, 0, COLS, ROWS+5);
	}
}
