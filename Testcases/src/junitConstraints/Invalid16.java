package junitConstraints;

import static security.Definition.*;

@Constraints({ "@pc <= low" })
public class Invalid16 {

	@Constraints({ "@pc <= low", "@0 <= low" })
	public static void main(String[] args) {}

	// constraints contain an invalid parameter reference for the second parameter
	@Constraints({ "@pc <= low", "@0 <= low", "@1 <= confidential" })
	public Invalid16(int arg1, int arg2) {}

}
