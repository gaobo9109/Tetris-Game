

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class StateStorage {
	private BufferedWriter fw;
	private BufferedReader fr;
	
	public void writeStateToFile(ArrayList<Heuristic> population, String fileName){
		try{
			fw = new BufferedWriter(new FileWriter(fileName));
			for(Heuristic hs : population){
				fw.write(hs.toString());
			}
		} catch (Exception e){
			System.out.println("Error in CsvFileWriter!");
			e.printStackTrace();
		} finally{
			try {
				fw.flush();
				fw.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter!");
                e.printStackTrace();
			}
		}
	}
	
	/**
	 * Load saved feature weights from file. 
	 * If file does not exist, create new set of feature weights
	 * 
	 * @param file name
	 * @return ArrayList of Heuristic
	 */
	
	public boolean readStateFromFile(String fileName,ArrayList<Heuristic> population, Metaheuristic.MetaheuristicTypes metaheuristicType){
		
		boolean fileExist = new File(fileName).isFile();
		if(!fileExist){
			System.out.println("File not found, creating new heuristic sets");
			return false;
		}
		
		String line = "";
		try{
			System.out.println("Loading heuristic from file...");
			fr = new BufferedReader(new FileReader(fileName));
			Feature[] features;
			
			while((line = fr.readLine()) != null){
				String[] tokens = line.split(",");
				Heuristic hs = new Heuristic();
				features = hs.getFeatures();
				
				if(tokens.length == features.length){					
					for (int i = 0; i < features.length; i++){
						double weight = Double.parseDouble(tokens[i]);
						features[i].setFeatureWeight(weight);
					}
					population.add(Heuristic.heuristicFactory(metaheuristicType, hs));
				}
			}
			System.out.println("Number of heuristic is " + population.size());
		} catch(Exception e) {
        	System.out.println("Error in csv FileReader!");
            e.printStackTrace();
		} finally {
			try {
                fr.close();
            } catch (IOException e) {
            	System.out.println("Error while closing fileReader!");
                e.printStackTrace();
            }
		}
		return true;
	}
}
