package taintTrackingFail;

import security.Annotations;
import security.SootSecurityLevel;


public class TaintTrackingIdMethods {
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int returnLowSecurity() {
		int high = SootSecurityLevel.highId(42);
		return high;
	}
	
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	public void changeSecurityHigh2Low() {
		int high = SootSecurityLevel.highId(42);
		int low = SootSecurityLevel.lowId(high);
		return;
	}
}
