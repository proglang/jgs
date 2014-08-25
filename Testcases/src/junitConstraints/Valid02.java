package junitConstraints;

import static security.Definition.*;

public class Valid02 {
	
	@FieldSecurity("high")
	public int highIField = mkHigh(42);

	public static void main(String[] args) {}

	@Constraints("@pc <= high")
	public Valid02() {}

}
