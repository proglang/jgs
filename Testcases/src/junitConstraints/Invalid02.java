package junitConstraints;

import static security.Definition.*;

@Constraints({ "@pc <= low" })
public class Invalid02 {

	@Constraints({ "@pc <= low", "@0 <= low" })
	public static void main(String[] args) {}

	// constraints do not contain a parameter reference
	@Constraints({"@pc <= low"})
	public Invalid02(int arg) {}

}
