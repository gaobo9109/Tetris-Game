package com.cs3243.tetris;

import com.cs3243.tetris.cluster.Cluster;

public class Evolution {
		
	private Thread c1;
	private Thread c2;
	private Thread c3;
	private Thread c4;
	
	public Evolution(int totalPopulation){
		int clusterPopulation = totalPopulation / 4;
		c1 = new Thread(new Cluster("c1",clusterPopulation),"t1");
		c2 = new Thread(new Cluster("c2",clusterPopulation),"t2");
		c3 = new Thread(new Cluster("c3",clusterPopulation),"t3");
		c4 = new Thread(new Cluster("c4",clusterPopulation),"t4");
	}

	public void runEvolution(){
		c1.start();
		c2.start();
		c3.start();
		c4.start();
	}
	
	public static void main(String[] args){
		Evolution evo = new Evolution(100);
		evo.runEvolution();
	}
}



