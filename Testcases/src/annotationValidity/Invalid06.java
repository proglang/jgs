package annotationValidity;

import security.Annotations.ParameterSecurity;

public class Invalid06 {
	
	@ParameterSecurity({"high", "low"})
	// method has a return security level
	public int methodInclusivParameter(int arg1, int arg2) {
		return arg1;
	}

}
