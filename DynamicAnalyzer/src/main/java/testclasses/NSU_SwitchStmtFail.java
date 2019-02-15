package testclasses;

import de.unifreiburg.cs.proglang.jgs.support.DynamicLabel;

/**
 * Special case of NSU in this implementation. In theory, it should throw NSU,
 * but in practice, it doesn't because of compiler optimization. This is understood,
 * and acceptable to us.
 * @author Nicolas Müller
 *
 */
public class NSU_SwitchStmtFail {

	public static void main(String[] args) {
		String y = "__";
		int x = DynamicLabel.makeHigh(1);
		switch (x) {
		  case 1: 
			  y += "Case 1"; 		// Should always NSU. However, NSU thrown only 
			  						// if switch has just one branch
			  break;
		  // default: 				    // if default case is removed, it does throw NSU
			  						// because then the compiler reuses the y variable
			  						// with default case, it introduces a new one.
			  						// if default is not removed, no NSU is thrown!
			  // y += "Case Def"; 
			  // break;				// this problem should be the same for if/while
			  						// statements!
		}
		
		@SuppressWarnings("unused")
		String z = y + "__";
	}
}
