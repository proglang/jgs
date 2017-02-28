package testclasses;

import utils.analyzer.HelperClass;

/**
 * Behavior which is not consistent with NSU Policy.
 * Does not pose security issue. Labeled as "desired behavoir" for now.
 * @author Nicolas Müller
 *
 */
public class NSU_IfStmtSpecialCase {
	public static void main(String[] args) {
		String y = "";
		int x = HelperClass.makeHigh(1);
		if (x == 1) {
			y = "Case 1";		// should NSU
		} else {
			y = "Case Def";		// but does not, because of snd branch which induced the compiler to 
								// introduce a new variable for y
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
