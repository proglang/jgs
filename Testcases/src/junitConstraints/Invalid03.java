package junitConstraints;

import static security.Definition.*;

@Constraints({ "@pc <= low" })
public class Invalid03 {

	@Constraints({ "@pc <= low", "@0 <= low" })
	public static void main(String[] args) {}

	// constraints do not contain a program counter reference
	@Constraints({"@0 <= low"})
	public Invalid03(int arg) {}

}
