package junitConstraints;

import static security.Definition.*;

public class SuccessSpecial01 {
	
	@FieldSecurity("high")
	private int high = 42;

	@FieldSecurity("low")
	private int low = 42;
	
	public static void main(String[] args) {}

	@Constraints({ "@pc <= low"})
	public void validSpecial1() {
		int x = this.high;
		x = this.low;
		this.low = x;
	}

}
