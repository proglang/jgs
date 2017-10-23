package testclasses;

import util.analyzer.HelperClass;

public class NonStaticMethodsFail {

	public static void main(String[] args) {
		NonStaticMethodsFail nsm = new NonStaticMethodsFail();
		nsm.nonStatic();
	}
	
	public void nonStatic() {
		int x = HelperClass.makeHigh(4);
		System.out.println("Called a non-static method..");
		System.out.println(x);
	}

}
