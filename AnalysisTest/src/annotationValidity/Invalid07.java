package annotationValidity;

import security.Annotations.ParameterSecurity;

public class Invalid07 {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}

	@ParameterSecurity({ "high" })
	// method has a return security level
	public int methodInclusivParameter(int arg) {
		return arg;
	}

}
// @error("The return security level definition of a method which returns a value is mandatory.")