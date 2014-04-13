package junitAnalysis;

import static security.Definition.*;

public class Invalid01 {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}
	
	// too few parameter security levels  
	public Invalid01(int arg) {}
	
}