package com.cs3243.tetris.metaheuristics;

import java.util.Random;

import com.cs3243.tetris.cluster.Cluster;

public abstract class Metaheuristic {
	protected Random rand = new Random();
	protected Cluster cluster = null;

	public enum MetaheuristicTypes {
		GENETIC, PSO
	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	public Cluster getCluster() {
		return cluster;
	}

	abstract public void createNextGen();
}
