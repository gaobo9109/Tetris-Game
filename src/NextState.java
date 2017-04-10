
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
