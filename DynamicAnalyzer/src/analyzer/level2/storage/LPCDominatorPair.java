package analyzer.level2.storage;


/**
 * Class to store the local PC with its corresponding dominator. When this dominator
 * occurs in the method body, the local PC is popped from the lPC stack. postDom is the
 * identity-value for the dominator.
 * @author Regina Koenig (2016)
 *
 */
public class LPCDominatorPair {
	
	
	Object secLevel;
	int postDominatorIdentity;
	
	public LPCDominatorPair(Object securityLevel, int postDominatorIdentity) {
		this.secLevel = securityLevel;
		this.postDominatorIdentity = postDominatorIdentity;
	}

	public Object getSecurityLevel() {
		return secLevel;
	}
	
	public int getPostDominatorIdentity() {
		return postDominatorIdentity;
	}

	/**
	 * Overridden toString for easier observation in debugger
	 */
	@Override
	public String toString() { 
		return secLevel.toString();
	}
}
