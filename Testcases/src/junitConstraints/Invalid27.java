package junitConstraints;

import security.Definition.Constraints;

public class Invalid27 {

	public static void main(String[] args) {}
	
	@Constraints({"low <= @return", "low = @return["})
	public int invalid29() {
		return 42;
	}

}
