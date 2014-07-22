package junitConstraints;

import security.Definition.FieldSecurity;

public class Invalid18 {

	@FieldSecurity({"high", "low"})
	public static int[] field;

	public static void main(String[] args) {}

}
