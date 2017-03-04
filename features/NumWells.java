package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

public class NumWells extends Feature {
	
	@Override
	public double getScore(NextState s) {
		
		int[][] field = s.getField();
		int[] top = s.getTop();
		
		int count = 0;
		for(int i=0; i<top.length; i++){
			if(i == 0 || i == top.length-1){
				if(i == 0 && top[i+1] > top[i]) count++;
				else if(i == top.length-1 && top[i-1] > top[i]) count++;
			} else{
				if(top[i-1] > top[i] && top[i+1] > top[i]) count++;
			}
			
		}
		
		return featureWeight * count;
	}

}
