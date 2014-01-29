package annotationValidity;

import security.Annotations.ParameterSecurity;

public class Invalid16 {
	
	@ParameterSecurity({"high"})
	// method has a return security level
	public static int methodInclusivParameter(int arg) {
		return arg;
	}

}
