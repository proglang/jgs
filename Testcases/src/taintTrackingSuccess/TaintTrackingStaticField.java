package taintTrackingSuccess;

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
		return low;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	public int returnLowSecurity2() {
		int low2 = low;
		return low2;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int returnHighSecurity() {
		return high;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	public int returnHighSecurity2() {
		int high2 = high;
		return high2;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	public void assignLowSecurity() {
		int low2 = SootSecurityLevel.lowId(42);
		low = low2;
		return;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	public void assignHighSecurity() {
		int low = SootSecurityLevel.lowId(42);
		high = low;
		return;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	public void assignHigh2Security() {
		int high2 = SootSecurityLevel.highId(42);
		high = high2;
		return;
	}
	
}
