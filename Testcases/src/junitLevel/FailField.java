package junitLevel;

import static security.Definition.*;

public class FailField {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}
	
	@ReturnSecurity("low")
	public int returnLowSecurity() {
		// @security("The returned value has a stronger security level than expected.")
		return high;
	}
	
	@ReturnSecurity("low")
	public int returnLowSecurity2() {
		int high2 = high;
		// @security("The returned value has a stronger security level than expected.")
		return high2;
	}
	
	@WriteEffect({"low"})
	public void assignHighSecurity() {
		int high2 = mkHigh(42);
		// @security("The security level of the assigned value is stronger than the security level of the field.")
		low = high2;
		return;
	}
	
	@FieldSecurity("low")
	public int low = 42;
	
	@FieldSecurity("high")
	public int high = 42;
	
	@WriteEffect({"low", "high"})
	public FailField() {
		super();
	}
	
}
