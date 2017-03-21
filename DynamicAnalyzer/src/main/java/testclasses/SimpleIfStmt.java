package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

public class SimpleIfStmt {
	
	public static void main(String[] args) {
		int i = 42;
		simpleIfStmt(i);
	}
	
	/**
	 * Simple if/else that should pass all static and runtime checks
	 * @param x input
	 * @return output
	 */
	public static boolean simpleIfStmt(int x) {
		boolean isPositive;
		x = DynamicLabel.makeHigh(x);
		isPositive = DynamicLabel.makeHigh(false);
		
		if (x >= 0) {
			isPositive = true;
		} 
		return isPositive;
	}

}
