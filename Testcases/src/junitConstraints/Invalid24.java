package junitConstraints;

import security.Definition.FieldSecurity;

public class Invalid24 {

	@FieldSecurity({ "high", "low" })
	public static int[][] field;

	public static void main(String[] args) {}

}
