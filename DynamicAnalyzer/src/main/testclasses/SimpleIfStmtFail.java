package main.testclasses;

import utils.HelperClass.HelperClass;

public class SimpleIfStmtFail {
	
	public static void main(String[] args) {
		simpleIfStmtFail(42);
	}
	
	/**
	 * Simple if/else, which leaks information
	 * @param x input
	 * @return output
	 */
	public static boolean simpleIfStmtFail(int x) {
		boolean isPositive = false;
		x = HelperClass.makeHigh(x);
		isPositive = HelperClass.makeHigh(isPositive);
		
		if (x >= 0) {
			isPositive = true;
			System.out.println("Result successfully set");
		} 
		return isPositive;
	}

}
