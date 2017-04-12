package com.cs3243.tetris.lspi.features;

import com.cs3243.tetris.NextState;

public class RowTransitions extends LSPIFeature {

	@Override
	public double getScore(NextState state, int[] action) {
		int[][] field = state.field;
		int result = 0;
		for (int i = 0; i < field.length; i++) {
			int[] row = field[i];
			
			for (int j = 0; j < row.length; j++) {
				if (row[j] == 0) continue;
				if (j != 0 && row[j - 1] != 0) continue;
				if (j != row.length - 1 && row[j + 1] != 0) continue;
				result++;
			}
		}
		
		return result;
	}

}
