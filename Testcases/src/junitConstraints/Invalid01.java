package junitConstraints;

import static security.Definition.*;

@Constraints({ "@pc <= low" })
public class Invalid01 {

	@Constraints({ "@pc <= low", "@0 <= low" })
	public static void main(String[] args) {}

	// no constraints (especially no pc)
	public Invalid01() {}

}
