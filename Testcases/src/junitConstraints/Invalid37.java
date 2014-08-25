package junitConstraints;

import static security.Definition.*;

@Constraints("@pc <= low")
public class Invalid37 {

	// @security("illegal flow from high to low")
	@FieldSecurity("low")
	public static int lowIField = mkHigh(42);

	public static void main(String[] args) {}

}
