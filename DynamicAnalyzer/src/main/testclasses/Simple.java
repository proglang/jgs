package main.testclasses;

import utils.analyzer.HelperClass;


public class Simple {
	/**
	 * Simple test-method which tries to print a message with high security level.
	 * Result should be an illegal flow exception.
	 * @param args Arguments will be ignored.
	 */
	public static void main(String[] args) {
		int x = HelperClass.makeHigh(3);
		int y;
		if (x > 0) {	// int y is not initialised, thus there is no check
						// if high_x flows into low_y
			y = x;
		}
	}
}
