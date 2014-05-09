package junitConstraints;

import static security.Definition.*;

public class Invalid10 {

	public static void main(String[] args) {}

	// constraints contain an invalid parameter reference for the second parameter
	@Constraints({ "@1 <= confidential" })
	public Invalid10(int arg1, int arg2) {}

}
