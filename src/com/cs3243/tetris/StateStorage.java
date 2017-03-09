package com.cs3243.tetris;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class StateStorage {
	private BufferedWriter fw;
	private BufferedReader fr;
	
	public void writeStateToFile(ArrayList<Heuristic> population, String fileName){
		try{
			fw = new BufferedWriter(new FileWriter(fileName));
			for(Heuristic hs : population){
				String line = "";
				for(int i=0; i<hs.features.length; i++){
					double weight = hs.features[i].getFeatureWeight();
					line += String.valueOf(weight);
					if(i == hs.features.length-1){
						line += "\n";
					}else{
						line += ",";
					}
				}
				fw.write(line);
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
	
	public boolean readStateFromFile(String fileName,ArrayList<Heuristic> population){
		
		boolean fileExist = new File(fileName).isFile();
		if(!fileExist){
			System.out.println("File not found, creating new heuristic sets");
			return false;
		}
		
		String line = "";
		try{
			System.out.println("Loading heuristic from file...");
			fr = new BufferedReader(new FileReader(fileName));
			while((line = fr.readLine()) != null){
				String[] tokens = line.split(",");
				Heuristic hs = new Heuristic();
				if(tokens.length == hs.features.length){					
					for(int i=0; i<hs.features.length; i++){
						double weight = Double.parseDouble(tokens[i]);
						hs.features[i].setFeatureWeight(weight);
					}
					population.add(hs);
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
