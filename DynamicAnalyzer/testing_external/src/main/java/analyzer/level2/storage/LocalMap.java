package analyzer.level2.storage;

import analyzer.level2.SecurityLevel;
import util.exceptions.InternalAnalyzerException;
import util.logging.L2Logger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Logger;



/**
 * The LocalMap stores the locals of a methodbody and its corresponding security levels.
 * These security Levels are stored in the OptinalSecurity class, which acts as a 
 * container. It provides a flag "isInitialized" and throw an Exception if security
 * level of uninitialized local is queried.
 * @author Regina König (2015), Nicolas Müller (2016)
 *
 */
public class LocalMap {
	
	private Logger logger = L2Logger.getLogger();
	
	private LinkedList<LPCDominatorPair> localPC = new LinkedList<LPCDominatorPair>();
	private HashMap<String, SecurityOptional> localMap = new HashMap<String, SecurityOptional>();
	
	public LocalMap() {
		localPC.push(new LPCDominatorPair(SecurityLevel.bottom() , -1));
		localMap.put("DEFAULT_LOW", new SecurityOptional(SecurityLevel.bottom(), true));
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
	public void insertLocal(String signature, Object securityLevel) {
		localMap.put(signature, new SecurityOptional(securityLevel, true) );
	}
	
	/**
	 * Insert a new local into localMap with default security-level.
	 * @param signature The signature of the local.
	 */
	public void insertLocal(String signature) {
		localMap.put(signature, new SecurityOptional(SecurityLevel.bottom(), true) );
	}

	/**
	 * Insert an uninitialized local into localMap with default security-level. 
	 * @param signature
	 */
	public void insertUninitializedLocal(String signature) {
		localMap.put(signature, new SecurityOptional(SecurityLevel.bottom(), false));
	}
	
	/**
	 * Initialize a local. Check if initialized is actually redundant, but logger needs it.
	 * @param signature
	 */
	public void initializeLocal(String signature) {
		if (!localMap.get(signature).isInitialized()) {
			logger.info("Local " + signature + " initialized");
			localMap.get(signature).initialize();
		}
	}
	
	/**
	 * Return local's initialized flag.
	 * @param signature
	 * @return true iff local is initialized
	 */
	public boolean checkIfInitialized(String signature) {
		return localMap.get(signature).isInitialized();
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
		initializeLocal(signature);
		return localMap.get(signature).getSecurityLevel();
	}
	
	public void setLevel(String signature, Object securitylevel) {
		localMap.put(signature, new SecurityOptional(securitylevel, true));
	}
	
	/**
	 * Print elements of localmap in a readable form.
	 */
	public void printElements() {
		for (Map.Entry<String, SecurityOptional> entry : localMap.entrySet()) {
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
