package analyzer.level2.storage;

import analyzer.level2.SecurityLevel;
import utils.exceptions.InternalAnalyzerException;
import utils.logging.L2Logger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Logger;



/**
 * The LocalMap stores the locals of a methodbody and its corresponding security levels.
 * @author Regina König (2015)
 *
 */
public class LocalMap {
	
	private Logger logger = L2Logger.getLogger();
	
	private LinkedList<LPCDominatorPair> localPC = new LinkedList<LPCDominatorPair>();
	private HashMap<String, Object> localMap = new HashMap<String, Object>();
	
	public LocalMap() {
		localPC.push(new LPCDominatorPair(SecurityLevel.bottom() , -1));
		localMap.put("DEFAULT_BOTTOM", SecurityLevel.bottom());
	}
	
	/**
	 * Check whether the lpc stack is empty. This method is invoked at the end of every method.
	 * If the stack is not empty, than the program is closed with an InternalAnalyzerException.
	 * At the end there should be only one element left since there is one bottom() 
	 * element at the beginning.
	 */
	public void checkisLPCStackEmpty() {
		localPC.pop();
		if (!localPC.isEmpty()) {
			int n = localPC.size();
			throw new InternalAnalyzerException("LocalPC stack is not empty at the "
					+ "end of the method. There are still " 
					+ n + " elements.");
		}
	}
	
	/**
	 * @return The first element of the LPC stack without removing it.
	 */
	public Object getLocalPC() {
		if (localPC == null || localPC.size() < 1) {
			throw new InternalAnalyzerException("LocalPCStack is empty");
		}
		return localPC.getFirst().getSecurityLevel();
	}
	
	/**
	 * Remove first element of LPC-list if the given identity-value matches.
	 * @param dominatorIdentity Identity of the expected first element in LPC-list.
	 */
	public void popLocalPC(int dominatorIdentity) {
		int n = localPC.size();
		if (n < 1) {
			throw new InternalAnalyzerException("localPC-stack is empty");
		}
		if (!dominatorIdentityEquals(dominatorIdentity)) {
			throw new InternalAnalyzerException(
					"Trying to pop LPC with wrong identity");
		}
		localPC.pop();
		logger.finer("Reduced stack size from " + n 
				+ " to " + localPC.size() + " elements.");
	}
	
	/**
	 * Push a localPC and its corresponding identity-value to the LPC-stack.
	 * @param securityLevel New security-level for the LPC.
	 * @param dominatorIdentity Its identity.
	 */
	public void pushLocalPC(Object securityLevel, int dominatorIdentity) {
		localPC.push(new LPCDominatorPair(securityLevel, dominatorIdentity));
	}
	
	/**
	 * Insert a new local into localMap.
	 * @param signature The signature of the local.
	 * @param securityLevel Its securitylevel.
	 */
	public void insertElement(String signature, Object securityLevel) {
		localMap.put(signature, securityLevel);
	}
	
	/**
	 * Insert a new local into localMap with default securitylevel bottom().
	 * @param signature The signature of the local.
	 */
	public void insertElement(String signature) {
		insertElement(signature, SecurityLevel.bottom());
	}
	
	/**
	 * Get the level of a local
	 * @param signature The signature of a local.
	 * @return The new securitylevel.
	 */
	public Object getLevel(String signature) {
		if (!localMap.containsKey(signature)) {
			throw new InternalAnalyzerException("Expected local " 
			+ signature + " not found in LocalMap");
		}
		return localMap.get(signature);
	}
	
	public void setLevel(String signature, Object securitylevel) {
		localMap.put(signature, securitylevel);
	}
	
	/**
	 * Print elements of localmap in a readable form.
	 */
	public void printElements() {
		for (Map.Entry<String, Object> entry : localMap.entrySet()) {
			System.out.println("Key " + entry.getKey() + " , Value: " 
					+ entry.getValue());
		}
	}
	
	/**
	 * Check whether the localMap contains the given local.
	 * @param local The signature of a local.
	 * @return Returns true if the localMap contains the given local.
	 *     Else returns false.
	 */
	public boolean contains(String local) {
		return localMap.containsKey(local);
	}
	
	/**
	 * Compare the given identity value with the first element on the LPC stack.
	 * @param dominatorIdentity The identity.
	 * @return true if the identity equals to the first element on the stack.
	 */
	public boolean dominatorIdentityEquals(int dominatorIdentity) {
		return localPC.getFirst().getPostDominatorIdentity() == dominatorIdentity;	
	}
}
