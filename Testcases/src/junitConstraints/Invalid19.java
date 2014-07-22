package junitConstraints;

import security.Definition.FieldSecurity;

public class Invalid19 {

	@FieldSecurity({ "low", "confidential"})
	public static int[][] field;

	public static void main(String[] args) {}

}
