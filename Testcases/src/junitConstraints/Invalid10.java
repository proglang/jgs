package junitConstraints;

import static security.Definition.*;

@Constraints({ "@pc <= low" })
public class Invalid10 {

	@Constraints({ "@pc <= low", "@0 <= low" })
	public static void main(String[] args) {}

	// constraints of constructor does not contain a program counter reference

}
