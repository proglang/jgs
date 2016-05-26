package main.testclasses;

import utils.analyzer.HelperClass;

public class SwitchStmtFail {

	public static void main(String[] args) {
		simpleLookupSwitch(2);
		simpleTableSwitch(3);
	}
	
	
	/**
	 * Simple llokup-switch-testcase.
	 * @param x input
	 * @return output
	 */
	public static int simpleLookupSwitch(int x) {
		int y = 0;
		x = HelperClass.makeHigh(x);
		switch (x) {
		  case 1: 
			  y = x++; 
			  break;
		  case 100: 
			  y--; 
			  break;
		  default: 
			  y = 100; 
			  break;
		}
		return y;
	}
	
	/**
	 * Simple table-switch-testcase.
	 * @param x input
	 * @return output
	 */
	public static int simpleTableSwitch(int x) {
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
