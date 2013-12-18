package presentation;

import security.SootSecurityLevel;
import security.Annotations.*;

@WriteEffect({})
public class Presentation2 {

	@ParameterSecurity({ "low" })
	@ReturnSecurity("low")
	@WriteEffect({})
	public static int ifReturn(int low) {
		boolean high = SootSecurityLevel.highId(false);
		if (high) {
			return low;
		}
		return low;
	}

}
