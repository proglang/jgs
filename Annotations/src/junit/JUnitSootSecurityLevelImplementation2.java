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
public class JUnitSootSecurityLevelImplementation2 extends SecurityLevel {

	@Override
	public String[] getOrderedSecurityLevels() {
		return new String[] {"void", "1*", "high", "normal", "low", "h()"};
	}
	
	public static <T> T highId(T obj) {
		return obj;
	}
	
	@ReturnSecurity("low")
	public <T> T lowId(T obj) {
		return obj;
	}
	
	@ReturnSecurity("normal")
	protected static <T> T normalId(T obj) {
		return obj;
	}
	
	@ReturnSecurity("hello")
	public static <T> T helloId(T obj) {
		return obj;
	}

}
