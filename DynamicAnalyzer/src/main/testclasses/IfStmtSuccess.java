package main.testclasses;

import utils.analyzer.HelperClass;

public class IfStmtSuccess {

	/**
	 * Main method. Calls multIfs with different arguments.
	 * @param args args is ignored in this method.
	 */
	public static void main(String[] args) {
		IfStmtSuccess thisObj = new IfStmtSuccess();
		
		// pass low security value
		thisObj.multIfs(1);
		thisObj.multIfs(3);
		thisObj.multIfs(5);
		thisObj.nestedIfs(4);
		
		// pass high security value
		thisObj.multIfs(HelperClass.makeHigh(1));
		thisObj.multIfs(HelperClass.makeHigh(3));
		thisObj.multIfs(HelperClass.makeHigh(5));
		thisObj.nestedIfs(HelperClass.makeHigh(4));
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
