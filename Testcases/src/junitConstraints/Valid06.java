package junitConstraints;

import static security.Definition.*;

public class Valid06 {
	
	@FieldSecurity("high")
	public static int highIField = mkHigh(42);

	public static void main(String[] args) {}

}
