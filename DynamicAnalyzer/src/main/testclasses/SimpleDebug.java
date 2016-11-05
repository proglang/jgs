package main.testclasses;

import utils.analyzer.HelperClass;


public class SimpleDebug {
	public static void main(String[] args) {
		int secret = HelperClass.makeHigh(42);
		if (secret > 0) {
			System.out.println(secret);
		} else {
			System.out.println(secret);
		}
	}
}
