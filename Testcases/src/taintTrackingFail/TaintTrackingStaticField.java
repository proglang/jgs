package taintTrackingFail;

import security.Annotations;
import security.SootSecurityLevel;

@Annotations.WriteEffect({"low", "high"})
public class TaintTrackingStaticField {
		
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int returnLowSecurity() {
		return high;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int returnLowSecurity2() {
		int high2 = high;
		return high2;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low", "high"})
	public void assignHighSecurity() {
		int high2 = SootSecurityLevel.highId(42);
		low = high2;
		return;
	}

	@Annotations.FieldSecurity("low")
	public static int low = 42;
	
	@Annotations.FieldSecurity("high")
	public static int high = 42;
	
}
