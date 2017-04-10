


/**
 * This heuristic calculates the total height of all columns.
 */
public class TotalColHeight extends Feature {
    
    private int sum = 0;

    @Override
    public double getScore() {
        return featureWeight * sum;
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        int[] top = s.getTop();
        sum += top[col];
        
    }

    @Override
    public void resetScore() {
        sum = 0;
        
    }
}
