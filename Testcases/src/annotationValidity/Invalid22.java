package annotationValidity;

import security.Annotations.ParameterSecurity;

public class Invalid22 {
	
	@ParameterSecurity({"high", "low"})
	// method has a return security level
	public static int methodInclusivParameter(int arg1, int arg2) {
		return arg1;
	}

}
