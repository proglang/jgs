package junitLevel;

import static security.Definition.*;

public class Invalid07 {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}

	@ParameterSecurity({ "high" })
	// method has a return security level
	public int methodInclusivParameter(int arg) {
		return arg;
	}

}