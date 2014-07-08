package exgradual;

import static security.Definition.*;

public class Hierarchy {
	
	@FieldSecurity("low")
	public int lowField;

//	public static class A {
//		
//		@Constraints({"high <= @0", "@0 <= @return"})
//		public int test(int i) {
//			return i;
//		}
//		
//	}
//	
//	public static class B extends A {
//		
//		@Override
//		@Constraints({"low <= @0", "high <= @return"})
//		public int test(int i) {
//			return i;
//		}
//		
//	}
	
	public void test(){
		boolean x = true;
		if (x) {
			x = mkHigh(false);
			lowField = 5;
		}
	}
	
	public static void main(String[] args) { }
	
}
