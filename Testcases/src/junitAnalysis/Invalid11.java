package junitAnalysis;

import static security.Definition.*;

public class Invalid11 {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}

	// too few parameter security levels
	@ReturnSecurity("high")
	public int methodInclusivParameter(int arg) {
		return arg;
	}

}