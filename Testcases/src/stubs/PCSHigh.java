package stubs;

import security.Definition.Constraints;
import security.Definition.FieldSecurity;

@Constraints("@pc <= high")
public class PCSHigh {
	
	@FieldSecurity("low")
	public static int lowSField;
	
	@FieldSecurity("high")
	public static int highSField;

	@Constraints("@pc <= low")
	public static void lowPC() {}

	@Constraints("@pc <= high")
	public static void highPC() {}

}
