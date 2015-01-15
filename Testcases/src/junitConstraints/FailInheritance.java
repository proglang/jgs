package junitConstraints;

import security.Definition.*;

public class FailInheritance extends stubs.MinimalFields {

	public static void main(String[] args) {}

	public static class Parent {

		@Constraints("high <= @return")
		public int failInheritance1() {
			return lowSField;
		}
		
		@Constraints("@0 <= low")
		public void failInheritance2(int a0) {}
		
		@Constraints("@pc <= low")
		public void failInheritance3(){ }

	}

	public static class ChildA extends Parent {

		@Constraints("low <= @return")
		public int failInheritance1() {
			return lowSField;
		}

		@Constraints("@0 <= high")
		public void failInheritance2(int a0) {}
		
		@Constraints("@pc <= high")
		public void failInheritance3(){ }
		
	}

}
// @security("successInheritance1 in class ChildA has invalid signature")
// @security("successInheritance2 in class ChildA has invalid signature")
// @security("successInheritance3 in class ChildA has invalid signature")