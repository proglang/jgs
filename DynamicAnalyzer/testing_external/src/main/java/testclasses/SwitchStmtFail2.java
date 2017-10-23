package testclasses;

import util.analyzer.HelperClass;

/**
 * Implicit leak in switch Stmt. Must throw IFCError
 * @author Nicolas Müller
 *
 */
public class SwitchStmtFail2 {

	public static void main(String[] args) {
		simpleLookupSwitchVoid(2); // what if we call it with arg 2?!
								   // The DA throws IFExcep with x = 2;
	}
	
	/**
	 * Simple lookup switch testcase.
	 * @param x input
	 */
	public static void simpleLookupSwitchVoid(int x) {
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
		System.out.println(y);
	}

}