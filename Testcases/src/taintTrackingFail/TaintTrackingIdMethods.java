package taintTrackingFail;

import security.Annotations;
import security.SootSecurityLevel;

@Annotations.WriteEffect({})
public class TaintTrackingIdMethods {
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int returnLowSecurity() {
		int high = SootSecurityLevel.highId(42);
		return high;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public void changeSecurityHigh2Low() {
		int high = SootSecurityLevel.highId(42);
		int low = SootSecurityLevel.lowId(high);
		return;
	}
	
}