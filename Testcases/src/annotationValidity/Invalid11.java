package annotationValidity;

import security.Annotations.ReturnSecurity;

public class Invalid11 {
	
	// too few parameter security levels
	@ReturnSecurity("high")
	public int methodInclusivParameter(int arg) {
		return arg;
	}

}
