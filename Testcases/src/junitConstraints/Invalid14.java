package junitConstraints;

import static security.Definition.*;

// constraints of class contains a invalid return reference
@Constraints({"@pc <= low", "@0 <= low"})
public class Invalid14 {

	@Constraints({ "@pc <= low", "@0 <= low" })
	public static void main(String[] args) {}

	@Constraints({"@pc <= low"})
	public Invalid14() {}

}
