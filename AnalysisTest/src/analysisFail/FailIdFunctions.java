package analysisFail;

import security.Annotations;
import security.SootSecurityLevel;
import security.Annotations.ParameterSecurity;

public class FailIdFunctions {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}
	
	@Annotations.ReturnSecurity("low")
	public int returnLowSecurity() {
		int high = SootSecurityLevel.highId(42);
		// @security("The returned value has a stronger security level than expected.")
		return high;
	}
	
	public void changeSecurityHigh2Low() {
		int high = SootSecurityLevel.highId(42);
		// @security("The security level of the ID-function argument should be weaker or equal to the level of the function.")
		SootSecurityLevel.lowId(high);
		return;
	}
	
}