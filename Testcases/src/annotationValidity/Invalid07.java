package annotationValidity;

import security.Annotations.ParameterSecurity;

public class Invalid07 {
	
	@ParameterSecurity({"high"})
	// method has a return security level
	public int methodInclusivParameter(int arg) {
		return arg;
	}

}
