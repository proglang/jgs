package analyzer.level2.storage;

import analyzer.level2.SecurityLevel;
import utils.exceptions.InternalAnalyzerException;
import utils.logging.L2Logger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Logger;


public class LocalMap {
	
	private Logger logger = L2Logger.getLogger();
	
	private LinkedList<LPCDominatorPair> localPC = new LinkedList<LPCDominatorPair>();
	private HashMap<String, SecurityLevel> lMap = new HashMap<String, SecurityLevel>();
	
	public LocalMap() {
		localPC.push(new LPCDominatorPair(SecurityLevel.LOW, -1));
	}
	
	/**
	 * Check whether the lpc stack is empty. This method is invoked at the end of every method. If
	 * the stack is not empty, than the program is closed with an InternalAnalyzerException.
	 */
	public void CheckisLPCStackEmpty() {
		localPC.pop(); // At the end there should be one element left.
		if (!localPC.isEmpty()) {
			int n = localPC.size();
			new InternalAnalyzerException("LocalPC stack is not empty at the end of the method. There are still " 
					+ n + " elements.");
		}
	}
	
	public SecurityLevel getLocalPC() {
		// return localPC;
		return localPC.getFirst().getSecurityLevel();
	}
	
	public void popLocalPC(int domHash) {
		int n = localPC.size();
		localPC.pop();
		logger.finer("Reduced stack size from " + n + " to " + localPC.size() + " elements.");
	}
	
	public void pushLocalPC(SecurityLevel l, int domHash) {
		localPC.push(new LPCDominatorPair(l, domHash));
	}
	
	public void insertElement(String signature, SecurityLevel level) {
		lMap.put(signature, level);
	}
	
	public void insertElement(String signature) {
		insertElement(signature, SecurityLevel.LOW);
	}
	
	public SecurityLevel getLevel(String signature) {
		System.out.println(signature);
		if (!lMap.containsKey(signature)) {
			logger.finer("Expected local not found in lMap");
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
	
	public boolean contains(String local) {
		return lMap.containsKey(local);
	}
	
	public boolean domHashEquals(int domHash) {
		return localPC.getFirst().getPostDomHashValue() == domHash;	
	}
}
