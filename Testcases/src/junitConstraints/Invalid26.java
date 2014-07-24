package junitConstraints;

import security.Definition.FieldSecurity;

public class Invalid26 {

	@FieldSecurity({ "low" })
	public int[] field;

	public static void main(String[] args) {}

}
