package junitConstraints;

import static security.Definition.*;

@Constraints({ "@pc <= low" })
public class Invalid04 {

	@Constraints({ "@pc <= low", "@0 <= low" })
	public static void main(String[] args) {}

	// constraints contain a return reference
	@Constraints({"@pc <= low", "@0 <= low", "@return <= low"})
	public Invalid04(int arg) {}

}
