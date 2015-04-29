package analyzer.level2.storage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import analyzer.level2.Level;


public class LocalMap {
	
	private LinkedList<Level> localPC = new LinkedList<Level>();
	private HashMap<String, Level> lMap = new HashMap<String, Level>();
	private Level returnLevel = Level.LOW;
	
	public LocalMap() {
		localPC.push(Level.LOW);
	}
	
	public void setReturnLevel(Level l) {
		returnLevel = l;
	}
	
	public Level getReturnLevel() {
		return returnLevel;
	}
	
	public Level setLocalPC(Level l) {
		// localPC = l;
		return localPC.set(0, l);
	}
	
	public Level getLocalPC() {
		// return localPC;
		return localPC.getFirst();
	}
	
	public void popLocalPC() {
		localPC.pop();
	}
	
	public void pushLocalPC(Level l) {
		localPC.push(l);
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
