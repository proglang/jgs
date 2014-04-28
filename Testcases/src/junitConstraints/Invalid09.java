package junitConstraints;

import static security.Definition.*;

@Constraints({ "@pc <= low" })
public class Invalid09 {

	@Constraints({ "@pc <= low", "@0 <= low" })
	public static void main(String[] args) {}

	// constraints contain an invalid parameter reference
	@Constraints({"@pc <= low", "@0 <= low"})
	public Invalid09() {}

}
