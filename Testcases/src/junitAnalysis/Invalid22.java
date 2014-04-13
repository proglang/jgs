package junitAnalysis;

import static security.Definition.*;

public class Invalid22 {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}

	@ParameterSecurity({ "high", "low" })
	// method has a return security level
	public static int methodInclusivParameter(int arg1, int arg2) {
		return arg1;
	}

}