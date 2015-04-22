package analyzer.level2.storage;

import java.util.HashMap;
import java.util.Map;

import analyzer.level2.Level;


public class LocalMap {
	
	private Level localPC = Level.LOW;
	private HashMap<String, Level> lMap = new HashMap<String, Level>();
	private Level returnLevel = Level.LOW;
	private Level returnLevelOfLastCallee = Level.LOW;
	
	public void setCalleeReturnLevel(Level l) {
		returnLevelOfLastCallee = l;
	}
	
	public Level getCalleeReturnLevel() {
		return returnLevelOfLastCallee;
	}
	
	public void setReturnLevel(Level l) {
		returnLevel = l;
	}
	
	public Level getReturnLevel() {
		return returnLevel;
	}
	
	public void setLocalPC(Level l) {
		localPC = l;
	}
	
	public Level getLocalPC() {
		return localPC;
	}
	
	public void insertElement(String signature, Level level) {
		lMap.put(signature, level);
	}
	
	public void insertElement(String signature) {
		insertElement(signature, Level.LOW);
	}
	
	public Level getLevel(String signature) {
		if (!lMap.containsKey(signature)) {
			lMap.put(signature, Level.LOW);
		}
		return lMap.get(signature);
	}
	
	public void setLevel(String signature, Level level) {
		lMap.put(signature, level);
	}
	
	public void printElements() {
		for(Map.Entry<String, Level> entry : lMap.entrySet()) {
			System.out.println("Key " + entry.getKey() + " , Value: " + entry.getValue());
		}
	}
}
