package junitConstraints;

import static security.Definition.*;

@Constraints({ "@pc <= low" })
public class Valid01 {

	@Constraints({ "@pc <= low", "@0 <= low" })
	public static void main(String[] args) {}

	@Constraints({ "@pc <= low" })
	public Valid01() {}

}
