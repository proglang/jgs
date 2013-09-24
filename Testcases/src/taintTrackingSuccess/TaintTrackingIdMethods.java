package taintTrackingSuccess;

import security.Annotations;
import security.SootSecurityLevel;

@Annotations.WriteEffect({})
public class TaintTrackingIdMethods {
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({})
	public int returnHighSecurity() {
		int high = SootSecurityLevel.highId(42);
		return high;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int returnLowSecurity() {
		int low = SootSecurityLevel.lowId(42);
		return low;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public void changeSecurityLow2High() {
		int low = SootSecurityLevel.lowId(42);
		int high = SootSecurityLevel.highId(low);
		return;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({})
	public void changeSecurityHigh2High() {
		int high = SootSecurityLevel.highId(42);
		int high2 = SootSecurityLevel.highId(high);
		return;
	}
}
