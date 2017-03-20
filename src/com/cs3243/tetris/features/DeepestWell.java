package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

/**
 * This heuristic calculates the height of the lowest column.
 */
public class DeepestWell extends Feature {
    
    private int maxWellDepth = 0;
    
    /*
	@Override
	public double getScore(NextState s) {
        int[] top = s.getTop();

		int maxWellDepth = 0;

		for (int i = 0; i < top.length; i++) {
			int wellDepth = 0;

      if (i == 0 && top[i + 1] > top[i]) {
        wellDepth = top[i + 1] - top[i];
      } else if (i == top.length - 1 && top[i - 1] > top[i]) {
        wellDepth = top[i - 1] - top[i];
			} else if (i != 0 && i != top.length - 1 && top[i - 1] > top[i] && top[i + 1] > top[i]) {
				wellDepth = Math.min(top[i - 1], top[i + 1]) - top[i]; 
			}

			maxWellDepth = Math.max(maxWellDepth, wellDepth);
		}

		return featureWeight * maxWellDepth;
	}
	*/

    @Override
    public double getScore() {
        return featureWeight * maxWellDepth;
    }

    @Override
    public void updateScore(NextState s, int row, int col) {
        int[] top = s.getTop();
        int wellDepth = 0;
        if (col == 0 && top[col + 1] > top[col]) {
            wellDepth = top[col + 1] - top[col];
        } else if (col == top.length - 1 && top[col - 1] > top[col]) {
            wellDepth = top[col - 1] - top[col];
        } else if (col != 0 && col != top.length - 1 && top[col - 1] > top[col] && top[col + 1] > top[col]) {
            wellDepth = Math.min(top[col - 1], top[col + 1]) - top[col]; 
        }

        maxWellDepth = Math.max(maxWellDepth, wellDepth);
    }

    @Override
    public void resetScore() {
       maxWellDepth = 0;
        
    }
}
