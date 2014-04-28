package junitConstraints;

import static security.Definition.*;

@Constraints({ "@pc <= low" })
public class Invalid17 {
	
	// no security level
	public int field;
	
	@Constraints({ "@pc <= low", "@0 <= low" })
	public static void main(String[] args) {}

	@Constraints({ "@pc <= low" })
	public Invalid17() {}

}
