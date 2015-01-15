package junitConstraints;

import static security.Definition.*;

@Constraints("@pc <= high")
public class Valid05 {
	
	@FieldSecurity("high")
	public static int highIField = mkHigh(42);

	public static void main(String[] args) {}

}
