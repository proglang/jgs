package junitConstraints;

import static security.Definition.*;

public class SuccessSpecial02 {
	
	public static void main(String[] args) {}
	
	@Constraints({ "high <= @return" })
	public int successSpecial1() {
		int[] arr = arrayIntHigh(42);
		return arr[23];
		// By removing the constraints which contains locals also the base of ComponentArrayRefs should be considered.
	}

}
