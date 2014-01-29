package annotationValidity;

import security.Annotations.ParameterSecurity;

public class Invalid14 {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}

	// no field security level
	public static int staticField = 42;

}
// @error("The security level of a field is mandatory.")