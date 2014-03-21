package junitAnalysis;

import security.Definition.*;

public class Invalid21 {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}

	@ParameterSecurity({ "high", "low" })
	// too many parameter security levels
	@ReturnSecurity("high")
	public static int methodInclusivParameter(int arg) {
		return arg;
	}

}
// @error("For each method parameter should exist exactly one security level.")