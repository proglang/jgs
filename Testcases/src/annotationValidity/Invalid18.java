package annotationValidity;

import security.Annotations.ReturnSecurity;

public class Invalid18 {
	
	// void method hasn't a return security level
	@ReturnSecurity("high")
	public static void voidMethod() {
		return;
	}
	
}
