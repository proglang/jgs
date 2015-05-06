package analyzer.level2.storage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import analyzer.level2.SecurityLevel;


public class LocalMap {
	
	private LinkedList<SecurityLevel> localPC = new LinkedList<SecurityLevel>();
	private HashMap<String, SecurityLevel> lMap = new HashMap<String, SecurityLevel>();
	private SecurityLevel returnLevel = SecurityLevel.LOW;
	
	public LocalMap() {
		localPC.push(SecurityLevel.LOW);
	}
	
	public void setReturnLevel(SecurityLevel l) {
		returnLevel = l;
	}
	
	public SecurityLevel getReturnLevel() {
		return returnLevel;
	}
	
	public SecurityLevel setLocalPC(SecurityLevel l) {
		// localPC = l;
		return localPC.set(0, l);
	}
	
	public SecurityLevel getLocalPC() {
		// return localPC;
		return localPC.getFirst();
	}
	
	public void popLocalPC() {
		localPC.pop();
	}
	
	public void pushLocalPC(SecurityLevel l) {
		localPC.push(l);
	}
	
	public void insertElement(String signature, SecurityLevel level) {
		lMap.put(signature, level);
	}
	
	public void insertElement(String signature) {
		insertElement(signature, SecurityLevel.LOW);
	}
	
	public SecurityLevel getLevel(String signature) {
		if (!lMap.containsKey(signature)) {
			lMap.put(signature, SecurityLevel.LOW);
		}
		return lMap.get(signature);
	}
	
	public void setLevel(String signature, SecurityLevel level) {
		lMap.put(signature, level);
	}
	
	public void printElements() {
		for(Map.Entry<String, SecurityLevel> entry : lMap.entrySet()) {
			System.out.println("Key " + entry.getKey() + " , Value: " + entry.getValue());
		}
	}
}
