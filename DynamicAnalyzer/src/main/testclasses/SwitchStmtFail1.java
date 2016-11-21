package main.testclasses;

import utils.analyzer.HelperClass;

/**
 * Implicit leak in switch Stmt. Must throw IllegalFlowException
 * @author Nicolas Müller
 *
 */
public class SwitchStmtFail1 {

	public static void main(String[] args) {
		int r = simpleLookupSwitch(1);	// what if we call it with arg 2?!
		System.out.println(r);
	}
	
	
	/**
	 * Simple lookup switch testcase.
	 * @param x input
	 * @return output
	 */
	public static int simpleLookupSwitch(int x) {
		int y = 0;
		x = HelperClass.makeHigh(x);
		switch (x) {
		  case 1: 
			  y = x; 
			  break;
		  default: 
			  y = 100; 
			  break;
		}
		return y;
	}
}
