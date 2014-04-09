package junitAnalysis;

import static security.Definition.*;

public class Invalid13 {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}

	// no field security level
	public int instanceField;

}
// @error("The security level of a field is mandatory.")