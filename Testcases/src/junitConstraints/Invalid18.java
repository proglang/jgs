package junitConstraints;

import static security.Definition.*;

@Constraints({ "@pc <= low" })
public class Invalid18 {
	
	// invalid security level
	@FieldSecurity("confidential")
	public int field;
	
	@Constraints({ "@pc <= low", "@0 <= low" })
	public static void main(String[] args) {}

	@Constraints({ "@pc <= low" })
	public Invalid18() {}

}
