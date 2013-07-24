package security;

import security.SecurityAnnotation.*;

public class SootSecurityLevel extends SecurityLevel {

	@Override
	public String[] getOrderedSecurityLevels() {
		return new String[] { "high", "low"};
	}

	public static void main(String[] args) {
		new SootSecurityLevel();
	}
	
	@ReturnSecurity("high")
	public static <T> T highId(T object) {
		return object;
	}
	
	@ReturnSecurity("low")
	public static <T> T lowId(T object) {
		return object;
	}

}
