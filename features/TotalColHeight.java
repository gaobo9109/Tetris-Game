package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

public class TotalColHeight extends Feature {
	
	@Override
	public double getScore(NextState s) {
		
		int[][] field = s.getField();
		int[] top = s.getTop();
		
		int sum = 0;
		for(int i:top){
			sum += i;
		}
		
		return featureWeight * sum;
	}

}
