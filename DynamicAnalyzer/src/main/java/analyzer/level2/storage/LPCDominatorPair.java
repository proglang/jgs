package analyzer.level2.storage;


/**
 * Class to store the local PC with its corresponding dominator. When this dominator
 * occurs in the method body, the local PC is popped from the lPC stack. postDom is the
 * identity-value for the dominator.
 * @author Regina Koenig (2016), Karsten Fix(2017)
 *
 */
// Todo: Find out, if really needed - in case not: remove it!
public class LPCDominatorPair<Level> {
	
	private Level secLevel;
	private int postDominatorIdentity;
	
	public LPCDominatorPair(Level securityLevel, int postDominatorIdentity) {
		this.secLevel = securityLevel;
		this.postDominatorIdentity = postDominatorIdentity;
	}

	public Level getSecurityLevel() {
		return secLevel;
	}
	
	int getPostDominatorIdentity() {
		return postDominatorIdentity;
	}

	/**
	 * Overridden toString for easier observation in debugger
	 */
	@Override
	public String toString() { 
		return secLevel.toString() + "@ID:" + postDominatorIdentity;
	}
}
