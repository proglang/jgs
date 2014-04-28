package junitConstraints;

import static security.Definition.*;

// constraints of class contains a invalid return reference
@Constraints({"@pc <= low", "@return <= low"})
public class Invalid13 {

	@Constraints({ "@pc <= low", "@0 <= low" })
	public static void main(String[] args) {}

	@Constraints({"@pc <= low"})
	public Invalid13() {}

}
