import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Evolution {
	
	private ArrayList<Heuristics> population;
	private int popSize;
	private PlayerSkeleton ps;
	private Random rand;
	private int generation;
	
	public Evolution(int popSize, int generation){
		this.popSize = popSize;
		this.generation = generation;
		population = new ArrayList<Heuristics>();
		ps = new PlayerSkeleton();
		rand = new Random();
		initPopulation();
	}
	
	private void initPopulation(){
		for(int i=0; i<popSize; i++){
			Heuristics hs = new Heuristics();
			population.add(hs);
		}
	}
	
	private void createNextGen(){
		double averageFitness = 0;
		for(int i=0; i<popSize; i++){
			Heuristics hs = population.get(i);
			double fitness = ps.playFullGame(hs, false);
			hs.setFitness(fitness);
			averageFitness += fitness;
			System.out.println("player "+i+" has a fitness value of "+fitness);
		}
		
		System.out.println("average fitness of this generation is "+averageFitness/popSize);
		
		int cutoff = (int)(popSize * 0.3);
		Collections.sort(population);
		
		//remove the bottom 30%
		for(int i=0; i<cutoff; i++){
			population.remove(0);
		}
		
		cutoff = cutoff * 2;
		ArrayList<Heuristics> subset = new ArrayList<Heuristics>();
		
		//get the fittest 60%
		for(int i=0; i<cutoff; i++){
			subset.add(population.get(population.size()-i-1));
		}
		
		Collections.shuffle(subset);
		
		
		for(int i=0; i<cutoff; i+=2){
			Heuristics hs1 = subset.get(i);
			Heuristics hs2 = subset.get(i+1);
			Heuristics child = Heuristics.mix(hs1, hs2);
			child.mutate();
			population.add(child);
		}
			
	}
	
	public void runEvolution(){
		for(int i=0; i<generation; i++){
			System.out.println("generation "+i);
			createNextGen();
		}
		
	}
	
	public Heuristics getBestHeuristics(){
		Collections.sort(population,Collections.reverseOrder());
		return population.get(0);
	}
	
	public static void main(String[] args){
		Evolution evo = new Evolution(20,500);
		evo.runEvolution();
		Heuristics hs = evo.getBestHeuristics();
		PlayerSkeleton ps = new PlayerSkeleton();
		ps.playFullGame(hs, true);
	}
	
	

}
