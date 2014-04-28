package junitLevel;

import static security.Definition.*;

public class Invalid14 {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}

	// no field security level
	public static int staticField = 42;

}