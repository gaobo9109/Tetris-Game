import java.util.Random;

public class Heuristics implements Comparable<Heuristics>{
	
	public static final int FEATURE_NUM = 6;
	public static final double MUTATION_PROB = 0.1;
	public static final double PERTURBATION_RANGE = 0.05;
	private double[] featureWeight = new double[FEATURE_NUM]; 
	private double fitness;
	private Random rand = new Random();
	
	public Heuristics(){
		//generate random weight
		
		for(int i=0; i<featureWeight.length; i++){
			featureWeight[i] = 2 * rand.nextDouble() - 1;
		}
		
		fitness = 0;
	}
	
	public Heuristics(double[] featureWeight){
		this.featureWeight = featureWeight;
		fitness = 0;
	}
	
	private int totalColHeight(int[] top){
		int sum = 0;
		for(int i:top){
			sum += i;
		}
		return sum;
	}
	
	private int totalColHeightDiff(int[] top){
		int sum = 0;
		for(int i=0; i<top.length-1; i++){
			sum += Math.abs(top[i+1] - top[i]);
		}
		return sum;
	}
	
	private int highestCol(int[] top){
		int maxCol = 0;
		for(int colHeight : top){
			maxCol = Math.max(maxCol, colHeight);
		}
		return maxCol;
	}
	
	private int numWells(int[] top){
		int count = 0;
		for(int i=0; i<top.length; i++){
			if(i == 0 || i == top.length-1){
				if(i == 0 && top[i+1] > top[i]) count++;
				else if(i == top.length-1 && top[i-1] > top[i]) count++;
			} else{
				if(top[i-1] > top[i] && top[i+1] > top[i]) count++;
			}
			
		}
		return count;
	}
	
	private int deepestWell(int[] top){
		int minWellRow = top[0];
		
		for(int i=0; i<top.length; i++){
			int wellRow = 0;
			
			if(i==0 && top[i+1] > top[i]) wellRow = top[i];
			else if(i==top.length-1 && top[i-1] > top[i]) wellRow = top[i];
			else if(top[i-1] > top[i] && top[i+1] > top[i]) wellRow = top[i];
			
			minWellRow = Math.min(minWellRow, wellRow);
			
		}
		return minWellRow;
	}
	
	private int numHoles(int[][] field, int[] top){
		int holes = 0;
		for(int i=0; i<top.length; i++){
			int colHeight = top[i];
			for(int j=0; j<colHeight-1; j++){
				//any empty cell beneath the top row cell in that col is a hole 
				if(field[j][i]==0) holes++;
			}
		}
		return holes;
	}
	
	private int colWithHole(int[][] field, int[] top){
		int count = 0;
		for(int i=0; i<top.length; i++){
			int colHeight = top[i];
			for(int j=0; j<colHeight-1; j++){
				if(field[j][i]==0){
					count ++;
					break;
				}
			}
		}
		return count;
	}
	
	public int calculateHeuristicScore(NextState s){
		int[][] field = s.getField();
		int[] top = s.getTop();
		int score = 0;
		
		score += featureWeight[0] * s.getRowsCleared();
		score += featureWeight[1] * totalColHeight(top);
		score += featureWeight[2] * totalColHeightDiff(top);
		score += featureWeight[3] * highestCol(top);
		score += featureWeight[4] * numWells(top);
//		score += featureWeight[5] * deepestWell(top);
//		score += featureWeight[6] * numHoles(field,top);
		score += featureWeight[5] * colWithHole(field,top);
		
		return score;
	}
	
	public void setFitness(double fitness){
		this.fitness = fitness;
	}
	
	public double getFitness(){
		return fitness;
	}
	
	public double[] getFeatureWeight(){
		return featureWeight;
	}
	
	public void setFeatureWeight(double[] featureWeight){
		this.featureWeight = featureWeight;
	}
	
	public void mutate(){
		for(int i=0; i<FEATURE_NUM; i++){
			boolean mutate = rand.nextDouble() < MUTATION_PROB;
			if(mutate){
				featureWeight[i] += rand.nextGaussian() * PERTURBATION_RANGE - PERTURBATION_RANGE;
			}
		}
	}
	
	public static Heuristics mix(Heuristics hs1, Heuristics hs2){
		double[] fw1 = hs1.getFeatureWeight();
		double ft1 = hs1.getFitness();
		double[] fw2 = hs2.getFeatureWeight();
		double ft2 = hs2.getFitness();
		
		double weightage = ft1 / (ft1 + ft2);
		double[] newWeight = new double[Heuristics.FEATURE_NUM];
		
		
		for(int i=0; i<Heuristics.FEATURE_NUM; i++){
			newWeight[i] = weightage * fw1[i] + (1-weightage) * fw2[i];
		}
		
		Heuristics newHeuristics = new Heuristics(newWeight);
		return newHeuristics;
		
	}
	
	@Override
    public int compareTo(Heuristics other) {
        return (int)(this.fitness - other.getFitness());
    }
	
	
	
}
