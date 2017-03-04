package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

public class ColWithHole extends Feature {
	
	@Override
	public double getScore(NextState s) {
		
		int[][] field = s.getField();
		int[] top = s.getTop();
		
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

		return featureWeight * count;
	}

}
