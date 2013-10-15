package junit;

import security.Annotations.ReturnSecurity;
import security.SecurityLevel;

/**
 * <h1> Valid JUnit test class</h1>
 * 
 * Represents a valid implementation of the {@link SecurityLevel} class.
 * 
 * @see PreTestSecurityLevelImplChecker
 * @author Thomas Vogel
 * @version 0.1
 */
public class JUnitSootSecurityLevelImplementation1 extends SecurityLevel {

	@Override
	public String[] getOrderedSecurityLevels() {
		return new String[] {"high", "normal", "low"};
	}
	
	@ReturnSecurity("high")
	public static <T> T highId(T obj) {
		return obj;
	}
	
	@ReturnSecurity("normal")
	public static <T> T normalId(T obj) {
		return obj;
	}
	
	@ReturnSecurity("low")
	public static <T> T lowId(T obj) {
		return obj;
	}
 
}
