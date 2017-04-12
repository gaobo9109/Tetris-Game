package com.cs3243.tetris.lspi.features;

import com.cs3243.tetris.NextState;

public class LandingHeight extends LSPIFeature {

	@Override
	public double getScore(NextState state, int[] action) {
		int[] top = NextState.pTop[state.nextPiece][action[0]];
		
		int result = 0;
		for (int i = 0; i < top.length; i++) {
			int height = 0;
			for (int j = state.field.length - 1; j >= 0; j--) {
				if (state.field[j][action[1]] != 0) {
					height = j;
					break;
				}
			}
			
			result = Math.max(result, height + top[i]);
		}
		
		return result;
	}

}
