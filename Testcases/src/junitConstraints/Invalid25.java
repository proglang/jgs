package junitConstraints;

import security.Definition.FieldSecurity;

public class Invalid25 {

	@FieldSecurity({ "high", "low" })
	public int[] field;

	public static void main(String[] args) {}

}
