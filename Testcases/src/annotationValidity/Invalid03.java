package annotationValidity;

import security.Annotations.ParameterSecurity;

public class Invalid03 {
	
	@ParameterSecurity({"high", "low"}) 
	// too many parameter security levels
	public Invalid03(int arg) {}

}
