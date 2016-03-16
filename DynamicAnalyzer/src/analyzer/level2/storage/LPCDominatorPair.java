package analyzer.level2.storage;

import analyzer.level2.SecurityLevel;

public class LPCDominatorPair {
	
	SecurityLevel secLevel;
	int postDom;
	
	public LPCDominatorPair(SecurityLevel secLevel, int postDom) {
		this.secLevel = secLevel;
		this.postDom = postDom;
	}

	public SecurityLevel getSecurityLevel() {
		return secLevel;
	}
	
	public int getPostDomHashValue() {
		return postDom;
	}
	
}
