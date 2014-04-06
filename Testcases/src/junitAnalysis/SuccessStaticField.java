package junitAnalysis;

import security.Definition;
import security.Definition.*;

@WriteEffect({"low", "high"})
public class SuccessStaticField {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}
	
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
	
	@WriteEffect({"low", "high"})
	public void assignLowSecurity() {
		int low2 = Definition.mkLow(42);
		low = low2;
		return;
	}
	
	@WriteEffect({"low", "high"})
	public void assignHighSecurity() {
		int low = Definition.mkLow(42);
		high = low;
		return;
	}
	
	@WriteEffect({"low", "high"})
	public void assignHigh2Security() {
		int high2 = Definition.mkHigh(42);
		high = high2;
		return;
	}
	
	@FieldSecurity("low")
	public static int low = 42;
	
	@FieldSecurity("high")
	public static int high = 42;
	
}
