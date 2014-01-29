package annotationValidity;

import security.Annotations.ParameterSecurity;
import security.Annotations.ReturnSecurity;

public class Invalid04 {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}

	@ReturnSecurity("high")
	// constructor hasn't a return security level
	public Invalid04() {}

}
// @error("The definition of a return security level for a constructor is not allowed.")