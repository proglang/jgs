package main.testclasses;

import utils.analyzer.HelperClass;

public class NSU_ForLoopSuccess {

	/**
	 * Test various for loops.
	 * @param args Not used
	 */
	public static void main(String[] args) {
		int secret = HelperClass.makeHigh(42);
		int res = simpleFor(secret);
	}

	/**
	 * Simple method with just one for-loop.
	 * @param x input
	 * @return output
	 */
	public static int simpleFor(int x) {
		for (int i = 0; i < x; i++) {
			x--;		
		}
		return x;
	}
}
