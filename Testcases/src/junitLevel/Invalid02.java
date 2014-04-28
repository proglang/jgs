package junitLevel;

import static security.Definition.*;

public class Invalid02 {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}

	@ParameterSecurity({})
	// too few parameter security levels
	public Invalid02(int arg) {
	}

}