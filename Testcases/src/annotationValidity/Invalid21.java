package annotationValidity;

import security.Annotations.ParameterSecurity;
import security.Annotations.ReturnSecurity;

public class Invalid21 {
	
	@ParameterSecurity({"high", "low"})
	// too many parameter security levels
	@ReturnSecurity("high")
	public static int methodInclusivParameter(int arg) {
		return arg;
	}

}
