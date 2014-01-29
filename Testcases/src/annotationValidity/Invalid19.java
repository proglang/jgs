package annotationValidity;

import security.Annotations.ReturnSecurity;

public class Invalid19 {
	
	// too few parameter security levels
	@ReturnSecurity("high")
	public static int methodInclusivParameter(int arg) {
		return arg;
	}

}
