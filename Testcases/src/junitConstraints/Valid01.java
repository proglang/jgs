package junitConstraints;

import static security.Definition.*;

public class Valid01 {
	
	@FieldSecurity("low")
	public int lowIField = 42;

	public static void main(String[] args) {}

	@Constraints("@pc <= low")
	public Valid01() {}

}
