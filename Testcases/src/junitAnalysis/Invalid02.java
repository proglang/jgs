package junitAnalysis;

import security.Definition.*;

public class Invalid02 {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}

	@ParameterSecurity({})
	// too few parameter security levels
	public Invalid02(int arg) {
	}

}
// @error("For each constructor parameter should exist exactly one security level.")