package analyzer.level2.storage;

import analyzer.level2.SecurityLevel;
import utils.exceptions.InternalAnalyzerException;
import utils.logging.L2Logger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Logger;

import de.unifreiburg.cs.proglang.jgs.constraints.SecDomain;


public class LocalMap {
	
	private Logger logger = L2Logger.getLogger();
	
	private LinkedList<LPCDominatorPair> localPC = new LinkedList<LPCDominatorPair>();
	private HashMap<String, SecurityLevel> localMap = new HashMap<String, SecurityLevel>();
	
	public LocalMap() {
		localPC.push(new LPCDominatorPair(SecurityLevel.LOW , -1));
		localMap.put("DEFAULT_LOW", SecurityLevel.LOW);
	}
	
	/**
	 * Check whether the lpc stack is empty. This method is invoked at the end of every method.
	 * If the stack is not empty, than the program is closed with an InternalAnalyzerException.
	 * At the end there should be only one element left since there is one LOW 
	 * element at the beginning.
	 */
	public void checkisLPCStackEmpty() {
		localPC.pop();
		if (!localPC.isEmpty()) {
			int n = localPC.size();
			new InternalAnalyzerException("LocalPC stack is not empty at the "
					+ "end of the method. There are still " 
					+ n + " elements.");
		}
	}
	
	/**
	 * @return The first element of the LPC stack without removing it.
	 */
	public SecurityLevel getLocalPC() {
		return localPC.getFirst().getSecurityLevel();
	}
	
	/**
	 * Remove first element of LPC-list if the given hashvalue matches.
	 * @param domHash Hashvalue of the expected first element in LPC-list.
	 */
	public void popLocalPC(int domHash) {
		int n = localPC.size();
		if (!domHashEquals(domHash)) {
			new InternalAnalyzerException("Trying to pop LPC with wrong hashvalue");
		}
		localPC.pop();
		logger.finer("Reduced stack size from " + n 
				+ " to " + localPC.size() + " elements.");
	}
	
	/**
	 * Push a localPC and its corresponding hash-value to the LPC-stack.
	 * @param l New Securitylevel for the LPC.
	 * @param domHash Its hashvalue.
	 */
	public void pushLocalPC(SecurityLevel l, int domHash) {
		localPC.push(new LPCDominatorPair(l, domHash));
	}
	
	/**
	 * Insert a new local into localMap.
	 * @param signature The signature of the local.
	 * @param level Its securitylevel.
	 */
	public void insertElement(String signature, SecurityLevel level) {
		localMap.put(signature, level);
	}
	
	/**
	 * Insert a new local into localMap with default securitylevel LOW.
	 * @param signature The signature of the local.
	 */
	public void insertElement(String signature) {
		insertElement(signature, SecurityLevel.LOW);
	}
	
	/**
	 * @param signature The signature of a local.
	 * @return The locals securitylevel.
	 */
	public SecurityLevel getLevel(String signature) {
		if (!localMap.containsKey(signature)) {
			/*logger.warning("Expected local " + signature + " not found in lMap. "
					+ "It's going to be inserted.");
			localMap.put(signature, SecurityLevel.LOW);*/
			new InternalAnalyzerException("Expected local " 
			+ signature + " not found in LocalMap");
		}
		return localMap.get(signature);
	}
	
	public void setLevel(String signature, SecurityLevel level) {
		localMap.put(signature, level);
	}
	
	/**
	 * Print elements of localmap in a readable form.
	 */
	public void printElements() {
		for (Map.Entry<String, SecurityLevel> entry : localMap.entrySet()) {
			System.out.println("Key " + entry.getKey() + " , Value: " 
					+ entry.getValue());
		}
	}
	
	/**
	 * @param local The signature of a local.
	 * @return Returns true if the localMap contains the given local.
	 *     Else returns false.
	 */
	public boolean contains(String local) {
		return localMap.containsKey(local);
	}
	
	public boolean domHashEquals(int domHash) {
		return localPC.getFirst().getPostDomHashValue() == domHash;	
	}
}
