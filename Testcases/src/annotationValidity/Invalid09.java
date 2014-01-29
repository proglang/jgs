package annotationValidity;

import security.Annotations.ParameterSecurity;
import security.Annotations.ReturnSecurity;

public class Invalid09 {
	
	@ParameterSecurity({"high", "low"})
	// too many parameter security levels
	@ReturnSecurity("high")
	public int methodInclusivParameter(int arg) {
		return arg;
	}

}
