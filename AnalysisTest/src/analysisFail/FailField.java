package analysisFail;

import security.Annotations;
import security.SootSecurityLevel;
import security.Annotations.ParameterSecurity;

public class FailField {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}
	
	@Annotations.ReturnSecurity("low")
	public int returnLowSecurity() {
		// @security("The returned value has a stronger security level than expected.")
		return high;
	}
	
	@Annotations.ReturnSecurity("low")
	public int returnLowSecurity2() {
		int high2 = high;
		// @security("The returned value has a stronger security level than expected.")
		return high2;
	}
	
	@Annotations.WriteEffect({"low"})
	public void assignHighSecurity() {
		int high2 = SootSecurityLevel.highId(42);
		// @security("The security level of the assigned value is stronger than the security level of the field.")
		low = high2;
		return;
	}
	
	@Annotations.FieldSecurity("low")
	public int low = 42;
	
	@Annotations.FieldSecurity("high")
	public int high = 42;
	
	@Annotations.WriteEffect({"low", "high"})
	public FailField() {
		super();
	}
	
}
