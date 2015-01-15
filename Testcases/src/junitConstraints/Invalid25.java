package junitConstraints;

import security.Definition.FieldSecurity;

public class Invalid25 {

	@FieldSecurity({ "low", "low", "high" })
	public int[] field;

	public static void main(String[] args) {}

}
