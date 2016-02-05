package main.testclasses;

public class IfStmt {

	/**
	 * Main method. Calls multIfs with different arguments.
	 * @param args args is ignored in this method.
	 */
	public static void main(String[] args) {
		IfStmt thisObj = new IfStmt();
		// int res = 0;
		thisObj.multIfs(1);
		thisObj.multIfs(3);
		thisObj.multIfs(5);
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
}
