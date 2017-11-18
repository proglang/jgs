package analyzer.level2.storage;

import analyzer.level2.CurrentSecurityDomain;
import util.exceptions.InternalAnalyzerException;
import util.logging.L2Logger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * The LocalMap
 * 1. maps the dynamically tracked locals of a method body to their current security levels.
 * 2. Maintains the stack of local pcs.
 *
 * Any local not present in the map is not dynamically tracked (i.e. statically checked).
 * A dynamically tracked local may be "uninitialized".
 *
 * @author Regina König (2015), Nicolas Müller (2016), fennell (2017)
 *
 */
public class LocalMap<L> {
	
	private Logger logger = L2Logger.getLogger();
	
	private LinkedList<LPCDominatorPair> localPC = new LinkedList<LPCDominatorPair>();
	private HashMap<String, L> localMap = new HashMap<>();
	
	public LocalMap() {
		localPC.push(new LPCDominatorPair(CurrentSecurityDomain.bottom() , -1));
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
	public void setLevel(String signature, L securityLevel) {
		localMap.put(signature, securityLevel);
	}

	/**
	 * Insert a new local into localMap with default security-level.
	 * @param signature The signature of the local.
	 */
	public void setToBottom(String signature) {
	    // TODO: parameterize (and un-static) "CurrentSecurityDomain" instead of casting
		localMap.put(signature, (L)CurrentSecurityDomain.bottom());
	}

	public void removeLocal(String signature) {
		localMap.remove(signature);
	}


	/**
	 * Return true iff the local is tracked.
	 */
	public boolean isTracked(String signature) {
		return localMap.containsKey(signature);
	}

	
	/**
	 * Get the level of a local
	 * @param signature The signature of a local.
	 * @return The new securitylevel.
	 */
	public L getLevel(String signature) {
		L result;
		if (!localMap.containsKey(signature)) {
			logger.log(Level.INFO, "Local `{0}' is not tracked", signature);
			result = (L)CurrentSecurityDomain.bottom();
		} else {
			result = localMap.get(signature);
		}
		logger.info("Getting label of " +  signature + " which is: " + result );
		return result;
	}
	

	/**
	 * Print elements of localmap in a readable form.
	 */
	public void printElements() {
		for (Map.Entry<String, L> entry : localMap.entrySet()) {
			System.out.println("Key " + entry.getKey() + " , Value: " 
					+ entry.getValue());
		}
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
