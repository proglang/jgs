package junitLevel;

import static security.Definition.*;

public class SuccessIdFunctions {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}
	
	@ReturnSecurity("high")
	public int returnHighSecurity() {
		int high = mkHigh(42);
		return high;
	}
	
	@ReturnSecurity("low")
	public int returnLowSecurity() {
		int low = mkLow(42);
		return low;
	}
	
	public void changeSecurityLow2High() {
		int low = mkLow(42);
		mkHigh(low);
		return;
	}
	
	public void changeSecurityHigh2High() {
		int high = mkHigh(42);
		mkHigh(high);
		return;
	}
}
