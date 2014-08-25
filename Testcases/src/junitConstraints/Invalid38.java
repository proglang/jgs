package junitConstraints;

import static security.Definition.*;

public class Invalid38 {

	// @security("illegal flow from high to low")
	@FieldSecurity("low")
	public int lowIField = mkHigh(42);

	public static void main(String[] args) {}
	
	@Constraints("@pc <= low")
	public Invalid38() {}

}
