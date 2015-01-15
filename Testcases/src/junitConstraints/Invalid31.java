package junitConstraints;

import static security.Definition.*;

public class Invalid31 {

	// @security("missing write effect to low")
	@FieldSecurity("low")
	public int lowIField = 42;

	public static void main(String[] args) {}

	public Invalid31() {}

}
