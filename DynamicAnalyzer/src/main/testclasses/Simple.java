package main.testclasses;

import com.sun.org.apache.xerces.internal.impl.dv.xs.YearDV;

import utils.analyzer.HelperClass;


public class Simple {
	/**
	 * Simple test-method which tries to print a message with high security level.
	 * Result should be an illegal flow exception.
	 * @param args Arguments will be ignored.
	 */
	public static void main(String[] args) {
		int x = HelperClass.makeHigh(3);
		int y = HelperClass.makeLow(3);
		if (x > 0) {
			y = x;
		}
	}
}
