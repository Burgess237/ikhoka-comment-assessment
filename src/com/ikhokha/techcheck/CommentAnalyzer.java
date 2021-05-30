package com.ikhokha.techcheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class CommentAnalyzer extends Thread {
	private Thread t;
	
	private File file;
	
	public CommentAnalyzer(File file) {
		this.file = file;
	}
	
	public void run() {
		analyze();
	}
	
	public Map<String, Integer> analyze() {
		
		Map<String, Integer> resultsMap = new HashMap<>();
		
		Map<String, String> testMap = new HashMap<>();
		// Use the Header and the key and the second value as the comparable value
		testMap.put("MOVER_MENTIONS", "mover");
		testMap.put("SHAKER_MENTIONS", "shaker");
		testMap.put("QUESTIONS", "?");
		testMap.put("SPAM", "http");
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			
			String line = null;
			while ((line = reader.readLine()) != null) {

				if (line.length() < 15) {
					
					incOccurrence(resultsMap, "SHORTER_THAN_15");

				} 
				
				containesTest(testMap, resultsMap, line);
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + file.getAbsolutePath());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Error processing file: " + file.getAbsolutePath());
			e.printStackTrace();
		}
		
		return resultsMap;
		
	}
	
	/**
	 * This method increments a counter by 1 for a match type on the countMap. Uninitialized keys will be set to 1
	 * @param countMap the map that keeps track of counts
	 * @param key the key for the value to increment
	 */
	private void incOccurrence(Map<String, Integer> countMap, String key) {
		
		countMap.putIfAbsent(key, 0);
		countMap.put(key, countMap.get(key) + 1);
	}
	
	private void containesTest(Map<String, String> testMap, Map<String, Integer> resultsMap, String line ) {
		testMap.forEach((header, compare) -> {
			if(line.toLowerCase().contains(compare)) 
			{
				incOccurrence(resultsMap, header);
			}
			});
	}
	
	public void start() {
		if(t == null) {
			t = new Thread (this);
			t.start();
		}
	}
	
}
