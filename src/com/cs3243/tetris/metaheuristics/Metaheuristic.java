package com.cs3243.tetris.metaheuristics;

import java.util.Random;

import com.cs3243.tetris.cluster.Cluster;

public abstract class Metaheuristic {
	protected Random rand = new Random();
	
	abstract public void createNextGen(Cluster cluster);
}
