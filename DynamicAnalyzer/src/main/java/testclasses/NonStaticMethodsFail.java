package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

public class NonStaticMethodsFail {

	public static void main(String[] args) {
		NonStaticMethodsFail nsm = new NonStaticMethodsFail();
		nsm.nonStatic();
	}
	
	public void nonStatic() {
		int x = DynamicLabel.makeHigh(4);
		System.out.println("Called a non-static method..");
		System.out.println(x);
	}

}
