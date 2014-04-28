package junitConstraints;

import static security.Definition.*;

@Constraints({ "@pc <= low" })
public class Invalid08 {

	@Constraints({ "@pc <= low", "@0 <= low" })
	public static void main(String[] args) {}

	// constraints contain an invalid security level for the program counter reference
	@Constraints({"@pc <= confidential", "@0 <= low"})
	public Invalid08(int arg) {}

}
