package junitAnalysis;

import static security.Definition.*;

public class Invalid20 {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}

	@ParameterSecurity({})
	// too few parameter security levels
	@ReturnSecurity("high")
	public static int methodInclusivParameter(int arg) {
		return arg;
	}

}