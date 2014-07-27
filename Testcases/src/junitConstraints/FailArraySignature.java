package junitConstraints;

import static security.Definition.*;
import security.Definition.FieldSecurity;

public class FailArraySignature extends stubs.Arrays {
    
    public static void main(String[] args) {}

	
	@Constraints({ "low <= @return" })
	public int test2() {
		int[] arr = arrayIntHigh(1);
		// @security("return is high")
		return arr[0];
	}

	public void test3() {
		int h = mkHigh(1);
		int[] arr = arrayIntLow(h);
	}

    @Constraints({ "low <= @return" })
    public int test1() {
        // @security("return is high")
        return highHigh[0];
    }
	

}
