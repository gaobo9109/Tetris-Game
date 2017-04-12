package com.cs3243.tetris.lspi.features;

import com.cs3243.tetris.NextState;

public class NumberOfHoles extends LSPIFeature {

	@Override
	public double getScore(NextState state, int[] action) {
		int[][] field = state.field;
		int result = 0;

		for (int i = 0; i < field[0].length; i++) {
			for (int j = field.length - 1; j >= 1; j--) {
				if (field[j][i] != 0 && field[j - 1][i] == 0) result++;
			}
		}
		
		return result;
	}

}
