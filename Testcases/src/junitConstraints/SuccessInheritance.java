package junitConstraints;

import security.Definition.*;

public class SuccessInheritance extends stubs.MinimalFields {
	
	public static void main(String[] args) {}
	
	public static class Parent {
		
		@Constraints("low <= @return")
		public int successInheritance1(){
			return lowSField;
		}
		
		@Constraints("low <= @return")
		public int successInheritance2(){
			return lowSField;
		}
		
		@Constraints("high <= @return")
		public int successInheritance3(){
			return lowSField;
		}
		
		@Constraints("@0 <= low")
		public void successInheritance4(int a0){ }
		
		@Constraints("@0 <= high")
		public void successInheritance5(int a0){ }
		
		@Constraints("@0 <= high")
		public void successInheritance6(int a0){ }
		
		@Constraints("@pc <= low")
		public void successInheritance7(){ }
		
		@Constraints("@pc <= high")
		public void successInheritance8(){ }
		
		@Constraints("@pc <= high")
		public void successInheritance9(){ }
		
	}
	
	public static class ChildA extends Parent {
		
		@Constraints("low <= @return")
		public int successInheritance1(){
			return lowSField;
		}
		
		@Constraints("high <= @return")
		public int successInheritance2(){
			return lowSField;
		}
		
		@Constraints("high <= @return")
		public int successInheritance3(){
			return lowSField;
		}
		
		@Constraints("@0 <= low")
		public void successInheritance4(int a0){ }
		
		@Constraints("@0 <= low")
		public void successInheritance5(int a0){ }
		
		@Constraints("@0 <= high")
		public void successInheritance6(int a0){ }
		
		@Constraints("@pc <= low")
		public void successInheritance7(){ }
		
		@Constraints("@pc <= low")
		public void successInheritance8(){ }
		
		@Constraints("@pc <= high")
		public void successInheritance9(){ }
		
	}

}
