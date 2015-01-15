package junitLevel;

import static security.Definition.*;

public class SuccessField {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}
	
	@FieldSecurity("low")
	public int low = 42;
	
	@FieldSecurity("high")
	public int high = 42;
	
	@WriteEffect({"low", "high"})
	public SuccessField() {
		super();
	}
	
	@ReturnSecurity("low")
	public int returnLowSecurity() {
		return low;
	}
	
	@ReturnSecurity("low")
	public int returnLowSecurity2() {
		int low2 = low;
		return low2;
	}
	
	@ReturnSecurity("high")
	public int returnHighSecurity() {
		return high;
	}
	
	@ReturnSecurity("high")
	public int returnHighSecurity2() {
		int high2 = high;
		return high2;
	}
	
	@WriteEffect({"low"})
	public void assignLowSecurity() {
		int low2 = mkLow(42);
		low = low2;
		return;
	}
	
	@WriteEffect({"high"})
	public void assignHighSecurity() {
		int low = mkLow(42);
		high = low;
		return;
	}
	
	@WriteEffect({"high"})
	public void assignHigh2Security() {
		int high2 = mkHigh(42);
		high = high2;
		return;
	}
}
