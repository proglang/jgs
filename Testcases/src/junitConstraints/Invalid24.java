package junitConstraints;

import security.Definition.FieldSecurity;

public class Invalid24 {

	@FieldSecurity({ "low" })
	public int[] field;

	public static void main(String[] args) {}

}
