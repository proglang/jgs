package main.testclasses;

import utils.analyzer.HelperClass;

public class ArithmeticExpressionsFail {
	
	/**
	 * @param args not used.
	 */
	public static void main(String[] args) {
		int res = HelperClass.makeHigh(3);
		if (res > 0) {
			res = (res + 1) * 42;
		}
		System.out.println(res);
	}
}
