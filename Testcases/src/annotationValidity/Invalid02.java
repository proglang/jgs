package annotationValidity;

import security.Annotations.ParameterSecurity;

public class Invalid02 {
	
	@ParameterSecurity({})
	// too few parameter security levels  
	public Invalid02(int arg) {}
	
}
