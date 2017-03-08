package com.cs3243.tetris.features;

import com.cs3243.tetris.NextState;

public class AltitudeDiff extends Feature {
	
	@Override
	public double getScore(NextState s){
		int[] top = s.getTop();
		int minHeight = top[0];
		int maxHeight = top[0];
		
		for(int i=1; i<top.length; i++){
			if(top[i] > maxHeight) maxHeight = top[i];
			else if(top[i] < minHeight) minHeight = top[i];
		}
		return maxHeight - minHeight;
	}
}
