package main.testclasses;

import utils.HelperClass.HelperClass;

public class IfStmtFail {

	/**
	 * Main method. Calls multIfs with different arguments.
	 * @param args args is ignored in this method.
	 */
	public static void main(String[] args) {
		IfStmtFail thisObj = new IfStmtFail();
		int sec = thisObj.multIfs(HelperClass.makeHigh(42));
		System.out.println(sec);
	}

	/**
	 * Simple method with IfStmt with several clauses.
	 * @param x A value of type int
	 * @return Calculated value
	 */
	public int multIfs(int x) {
		if (x < 0) {
			x = 0;
		} else if (x < 2) {
			x = 2;
		} else if (x < 4) {
			x = 4;
		} else {
			x = 6;
		}
		return x;
	}
	
	/**
	 * Simple method with nested if statements.
	 * @param x input value
	 * @return output value
	 */
	public int nestedIfs(int x) {
		if (x < 0) {
			x = 4;
			if (x < 5) {
				x = 5;
				if (x < 10) {
					x = 6 ;
				}
			}
		}
		return x;
	}
}
