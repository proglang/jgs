package junitAnalysis;

import security.Definition.*;

public class Invalid12 {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}

	// no field security level
	public static int staticField;

}
// @error("The security level of a field is mandatory.")