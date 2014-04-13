package junitAnalysis;

import static security.Definition.*;

public class Invalid12 {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}

	// no field security level
	public static int staticField;

}