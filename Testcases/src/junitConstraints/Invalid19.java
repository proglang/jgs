package junitConstraints;

import static security.Definition.*;

@Constraints({ "@pc <= low" })
public class Invalid19 {
	
	// invalid security level
	@FieldSecurity("confidential")
	public static int field;
	
	@Constraints({ "@pc <= low", "@0 <= low" })
	public static void main(String[] args) {}

	@Constraints({ "@pc <= low" })
	public Invalid19() {}

}
