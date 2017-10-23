package testclasses;

import util.analyzer.HelperClass;

public class FieldWriteFail {
	static int field = 0;
	
	public static void main(String[] args) {
		method(3);
	}
	
	/**
	 * Write a High value to a Low with in a High Context.
	 * @param x Integer
	 */
	public static void method(int x) {
		int y = HelperClass.makeHigh(x);
		if (y > 2) {
			field += y;
		}
		System.out.println(field);
	}
}
