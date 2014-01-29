package annotationValidity;

import security.Annotations.ReturnSecurity;

public class Invalid05 {
	
	// void method hasn't a return security level
	@ReturnSecurity("high")
	public void voidMethod() {
		return;
	}
	
}
