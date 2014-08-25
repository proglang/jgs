package junitConstraints;

import static security.Definition.*;

public class Invalid32 {

	// @security("missing write effect to low")
	// @security("illegal flow from high to low")
	@FieldSecurity("low")
	public int lowIField = mkHigh(42);

	public static void main(String[] args) {}

	public Invalid32() {}

}
