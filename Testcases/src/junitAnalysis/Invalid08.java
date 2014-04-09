package junitAnalysis;

import static security.Definition.*;

public class Invalid08 {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}

	// method has a return security level
	public int method() {
		return 42;
	}

}
// @error("The return security level definition of a method which returns a value is mandatory.")