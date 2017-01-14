package testclasses;

import utils.analyzer.HelperClass;

public class SimpleIfStmt {
	
	public static void main(String[] args) {
		simpleIfStmt(42);
	}
	
	/**
	 * Simple if/else that should pass all static and runtime checks
	 * @param x input
	 * @return output
	 */
	public static boolean simpleIfStmt(int x) {
		boolean isPositive = false;
		x = HelperClass.makeHigh(x);
		isPositive = HelperClass.makeHigh(isPositive);
		
		if (x >= 0) {
			isPositive = true;
		} 
		return isPositive;
	}

}
