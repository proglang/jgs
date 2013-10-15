package junit;

import security.Annotations.ReturnSecurity;
import security.SecurityLevel;

/**
 * <h1> Invalid JUnit test class</h1>
 * 
 * Represents an invalid implementation of the {@link SecurityLevel} class.
 * 
 * @see PreTestSecurityLevelImplChecker
 * @author Thomas Vogel
 * @version 0.1
 */
public class JUnitSootSecurityLevelImplementation4 extends SecurityLevel {

	@Override
	public String[] getOrderedSecurityLevels() {
		return new String[] {"high", "low"};
	}
	
	@ReturnSecurity("low")
	public static <T> T highId(T obj) {
		return obj;
	}
	
	@ReturnSecurity("high")
	public static <T> T lowId(T obj) {
		return obj;
	}

}
