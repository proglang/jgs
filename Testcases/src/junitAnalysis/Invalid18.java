package junitAnalysis;

import static security.Definition.*;

public class Invalid18 {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}

	// void method hasn't a return security level
	@ReturnSecurity("high")
	public static void voidMethod() {
		return;
	}

}