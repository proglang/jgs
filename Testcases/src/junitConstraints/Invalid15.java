package junitConstraints;

import static security.Definition.*;

@Constraints({"@pc <= low"})
public class Invalid15 {

	@Constraints({ "@pc <= low", "@0 <= low" })
	public static void main(String[] args) {}

	// constraints does not contain a parameter reference for the second parameter
	@Constraints({"@pc <= low", "@0 <= low"})
	public Invalid15(int arg1, int arg2) {}

}
