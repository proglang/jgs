package junitConstraints;

import static security.Definition.*;

// constraints of class contains a invalid security level
@Constraints({"@pc <= confidential"})
public class Invalid12 {

	@Constraints({ "@pc <= low", "@0 <= low" })
	public static void main(String[] args) {}

	@Constraints({"@pc <= low"})
	public Invalid12() {}

}
