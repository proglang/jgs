package taintTrackingSuccess;

import security.Annotations;
import security.SootSecurityLevel;

@Annotations.WriteEffect({})
public class TaintTrackingField {
	
	@Annotations.FieldSecurity("low")
	public int low = 42;
	
	@Annotations.FieldSecurity("high")
	public int high = 42;
	
	@Annotations.ParameterSecurity({})
	@Annotations.WriteEffect({"low", "high"})
	public TaintTrackingField() {
		super();
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int returnLowSecurity() {
		return low;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("low")
	@Annotations.WriteEffect({})
	public int returnLowSecurity2() {
		int low2 = low;
		return low2;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({})
	public int returnHighSecurity() {
		return high;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("high")
	@Annotations.WriteEffect({})
	public int returnHighSecurity2() {
		int high2 = high;
		return high2;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"low"})
	public void assignLowSecurity() {
		int low2 = SootSecurityLevel.lowId(42);
		low = low2;
		return;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"high"})
	public void assignHighSecurity() {
		int low = SootSecurityLevel.lowId(42);
		high = low;
		return;
	}
	
	@Annotations.ParameterSecurity({})
	@Annotations.ReturnSecurity("void")
	@Annotations.WriteEffect({"high"})
	public void assignHigh2Security() {
		int high2 = SootSecurityLevel.highId(42);
		high = high2;
		return;
	}
}
