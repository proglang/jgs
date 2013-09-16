package taintTrackingFail;

import security.Annotations;
import security.SootSecurityLevel;

public class TaintTrackingStaticField {
	
	@Annotations.FieldSecurity("low")
	public static int low = 42;
	
	@Annotations.FieldSecurity("high")
	public static int high = 42;
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int returnLowSecurity() {
		return high;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int returnLowSecurity2() {
		int high2 = high;
		return high2;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	public void assignHighSecurity() {
		int high2 = SootSecurityLevel.highId(42);
		low = high2;
		return;
	}
	
}
