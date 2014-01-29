package annotationValidity;

import security.Annotations.ReturnSecurity;

public class Invalid04 {

	@ReturnSecurity("high")
	// constructor hasn't a return security level
	public Invalid04() {}

}
