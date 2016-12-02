package main.testclasses;

import utils.analyzer.HelperClass;

public class NSU_ForLoopFail {

	/**
	 * Test various for loops.
	 * @param args Not used
	 */
	public static void main(String[] args) {
		int secret = HelperClass.makeHigh(42);
		int res = simpleFor(secret);
		System.out.println(res);
	}

	/**
	 * Simple method with just one for-loop.
	 * @param x input
	 * @return output
	 */
	public static int simpleFor(int x) {
		int i = 0;
		for (int j = 0; j < x; j++) {
			i--;
		}
		return i;
	}
}
