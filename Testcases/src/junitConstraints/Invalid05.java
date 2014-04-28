package junitConstraints;

import static security.Definition.*;

@Constraints({ "@pc <= low" })
public class Invalid05 {

	@Constraints({ "@pc <= low", "@0 <= low" })
	public static void main(String[] args) {}

	// constraints contain an invalid security level for the parameter reference
	@Constraints({"@pc <= low", "@0 <= confidential"})
	public Invalid05(int arg) {}

}
