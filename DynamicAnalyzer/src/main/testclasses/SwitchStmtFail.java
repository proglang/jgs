package main.testclasses;

import utils.analyzer.HelperClass;

public class SwitchStmtFail {

	public static void main(String[] args) {
		simpleSwitch(3);
	}
	
	/**
	 * Simple switch-testcase.
	 * @param x input
	 * @return output
	 */
	public static int simpleSwitch(int x) {
		int y = 0;
		x = HelperClass.makeHigh(x);
		switch (x) {
		  case 1: 
			  y = x++; 
			  break;
		  case 2: 
			  y--; 
			  break;
		  default: 
			  y = 100; 
			  break;
		}
		return y;
	}

}
