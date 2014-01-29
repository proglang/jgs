package annotationValidity;

import security.Annotations.ParameterSecurity;
import security.Annotations.ReturnSecurity;

public class Invalid18 {
	
	@ParameterSecurity({"low"})
	public static void main(String[] args) {}

	// void method hasn't a return security level
	@ReturnSecurity("high")
	public static void voidMethod() {
		return;
	}

}
// @error("The definition of a return security level for a void method is not allowed")