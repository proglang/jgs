package main.testclasses;

import utils.HelperClass.HelperClass;


public class DominatorNullPointer {
	public static void main(String[] args) {
		int secret = HelperClass.makeHigh(42);
		int y = returnInt(secret);
		
	}
	
	public static int returnInt(int x) {
		if (x > 0) {
			return x;
		} else {
			return 5;
		}
	}
}