package junitConstraints;

import static security.Definition.*;

// constraints of class does not contain a program reference
public class Invalid11 {

	@Constraints({ "@pc <= low", "@0 <= low" })
	public static void main(String[] args) {}

	@Constraints({"@pc <= low"})
	public Invalid11() {}

}
