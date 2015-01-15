package junitConstraints;

import static security.Definition.*;

public class Invalid02 {

	public static void main(String[] args) {}

	// constraints contain an invalid security level for the parameter reference
	@Constraints({ "@0 <= confidential" })
	public Invalid02(int arg) {}

}
