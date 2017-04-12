package com.cs3243.tetris.lspi.features;

import com.cs3243.tetris.NextState;

public class CoveredEmptyCells extends LSPIFeature {

	@Override
	public double getScore(NextState state, int[] action) {
		int[][] field = state.field;
		int result = 0;

		for (int i = 0; i < field[0].length; i++) {
			int top = 0;
			for (int j = field.length - 1; j >= 0; j--) {
				if (field[j][i] != 0) top = j;
				if (top != 0 && field[j][i] == 0) result++;
			}
		}
		
		return result;
	}

}
