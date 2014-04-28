package junitConstraints;

import static security.Definition.*;

@Constraints({ "@pc <= low" })
public class Invalid06 {

	@Constraints({ "@pc <= low", "@0 <= low" })
	public static void main(String[] args) {}

	// constraints contain an invalid additional parameter reference
	@Constraints({"@pc <= low", "@0 <= low", "@1 <= low"})
	public Invalid06(int arg) {}

}
