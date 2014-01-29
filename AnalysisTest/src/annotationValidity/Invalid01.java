package annotationValidity;

import security.Annotations.ParameterSecurity;

public class Invalid01 {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}
	
	// too few parameter security levels  
	public Invalid01(int arg) {}
	
}
// @error("For each constructor parameter should exist exactly one security level.")