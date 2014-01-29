package annotationValidity;

import security.Annotations.ParameterSecurity;

public class Invalid22 {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}

	@ParameterSecurity({ "high", "low" })
	// method has a return security level
	public static int methodInclusivParameter(int arg1, int arg2) {
		return arg1;
	}

}
// @error("The return security level definition of a method which returns a value is mandatory.")