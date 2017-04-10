


/*
 * Counting unoccupied -> occupied and occupied -> unoccupied transition within a row
 */

public class RowTransition extends Feature{
	private int count = 0;
	
    @Override
    public double getScore() {
        return featureWeight * count;
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        int[][] field = s.getField();
        int cols = field[0].length;
        
        if(col == 0 && field[row][col] == 0) count ++;
        else if(col == cols - 1 && field[row][col] == 0) count ++;
        
        if(col != cols -1){
            if(field[row][col] == 0 && field[row][col+1] != 0) count ++;
            else if(field[row][col] != 0 && field[row][col+1] == 0) count ++;
        }
        
    }

    @Override
    public void resetScore() {
        count = 0;
        
    }

}
