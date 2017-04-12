package com.cs3243.tetris.lspi.features;

import com.cs3243.tetris.NextState;

public class HeightSum extends LSPIFeature {

	@Override
	public double getScore(NextState state, int[] action) {
		int[][] field = state.field;
		int result = 0;
		int[] heights = new int[field[0].length];

		for (int i = 0; i < field[0].length; i++) {
			for (int j = field.length - 1; j >= 0; j--) {
				if (field[j][i] != 0) {
					heights[i] = j;
					break;
				}
			}
		}
		
		for (int i = 0; i < heights.length; i++) {
			result += heights[i];
		}
		
		return result;
	}

}
