package junitConstraints;

import static security.Definition.*;

public class FailMethod {

	public static void main(String[] args) {}
	
	
	public void test() {
		int[] arr = new int[] {1,2,3};
		Object arr1 = (Object) arr;
		int[] arr2 = (int[]) arr1;
		
		
	}

////	@Constraints({"high <= @return"})
//	public int test() {
//		int i = mkLow(42);
//		if (mkHigh(true)) {
//			
//			i = 42;
//		}
//		return i;
//	}
	
//	@FieldSecurity("low")
//	public int low;
//	
//	@FieldSecurity("high")
//	public int high;
//	
//	public void test() {
//		low = 42;
//	}
//	
//	@Constraints({"@pc <= low"})
//	public void test1() {
//		low = 42;
//		if (high == 0) {
//			high = 1;
//		}
//	}
	
}
