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