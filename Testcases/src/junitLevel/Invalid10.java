package junitLevel;

import static security.Definition.*;

public class Invalid10 {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}

	@ParameterSecurity({})
	// too few parameter security levels
	@ReturnSecurity("high")
	public int methodInclusivParameter(int arg) {
		return arg;
	}

}