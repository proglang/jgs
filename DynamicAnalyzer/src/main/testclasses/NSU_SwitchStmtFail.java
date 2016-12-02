package main.testclasses;

import utils.analyzer.HelperClass;

/**
 * Implicit leak in switch Stmt. Must throw IllegalFlowException
 * @author Nicolas Müller
 *
 */
public class NSU_SwitchStmtFail {

	public static void main(String[] args) {
		String y = "__";
		int x = HelperClass.makeHigh(1);
		switch (x) {
		  case 1: 
			  y += "Case 1"; 		// Should NSU!
			  break;
		  default: 					// if default case is removed, it does throw NSU
			  						// because then the compiler reuses the y variable
			  						// with default case, it introduces a new one
			  y += "Case Def"; 
			  break;
		}
		
		@SuppressWarnings("unused")
		String z = y + "__";
	}
}
