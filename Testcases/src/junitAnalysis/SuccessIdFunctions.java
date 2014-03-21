package junitAnalysis;

import security.Definition;
import security.Definition.*;

public class SuccessIdFunctions {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}
	
	@ReturnSecurity("high")
	public int returnHighSecurity() {
		int high = Definition.highId(42);
		return high;
	}
	
	@ReturnSecurity("low")
	public int returnLowSecurity() {
		int low = Definition.lowId(42);
		return low;
	}
	
	public void changeSecurityLow2High() {
		int low = Definition.lowId(42);
		Definition.highId(low);
		return;
	}
	
	public void changeSecurityHigh2High() {
		int high = Definition.highId(42);
		Definition.highId(high);
		return;
	}
}
