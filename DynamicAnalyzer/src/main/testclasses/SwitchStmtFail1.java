package main.testclasses;

import utils.analyzer.HelperClass;

/**
 * Implicit leak in switch Stmt. Must throw IllegalFlowException
 * @author Nicolas Müller
 *
 */
public class SwitchStmtFail1 {

	public static void main(String[] args) {
		String y = "";
		int x = HelperClass.makeHigh(1);
		switch (x) {
		  case 1: 
			  y = "Case 1"; 
			  break;
		  default: 
			  y = "Case Def"; 
			  break;
		}
		
		/**
		 * This is necessary, because otherwise the compiler will just
		 * throw away the updates of y inside the if, which will circumvent the
		 * IllegalFlowException
		 */
		@SuppressWarnings("unused")
		String z = y + "i exist only so that the compiler wont optimize y away";
	}
}
